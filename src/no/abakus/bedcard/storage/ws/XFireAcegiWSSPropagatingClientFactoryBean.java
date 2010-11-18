package no.abakus.bedcard.storage.ws;
/**
 * (c) Copyright 2006, Michael Vorburger.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.lang.reflect.Proxy;
import java.util.Properties;

import org.aopalliance.aop.Advice;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.client.XFireProxy;
import org.codehaus.xfire.security.wss4j.WSS4JOutHandler;
import org.codehaus.xfire.spring.remoting.XFireClientFactoryBean;
import org.codehaus.xfire.util.dom.DOMOutHandler;
import org.springframework.aop.framework.ProxyFactory;

/**
 * A XFireClientFactoryBean that adds the WSSAcegiPropagationAdvice via AOP.
 * 
 * @see WSSAcegiPropagationAdvice
 * @author Michael Vorburger
 */
public class XFireAcegiWSSPropagatingClientFactoryBean extends XFireClientFactoryBean {
	// Unfortunately this is private instead protected in the superclass, so I have to repeat it
	protected Object myServiceProxy;
	private String username;
	private String password;
	
	
	public XFireAcegiWSSPropagatingClientFactoryBean(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		
		// ?? myServiceProxy = ProxyFactory.getProxy(getServiceClass(), interceptor);
		Object client = super.getObject();
		Advice advice = new WSSAcegiPropagationAdvice(username, password);
		addOutHandlers(client);
		
		ProxyFactory proxyFactory = new ProxyFactory(client);
		proxyFactory.addAdvice(advice);
		myServiceProxy = proxyFactory.getProxy();
	}

	// TODO Make this more configurable... ideally have all of this in the service.xml, not here
	private void addOutHandlers(Object service) {
        Properties properties = new Properties();
        // NOTE: The ACTION is also set in the WSSAcegiPropagationAdvice
        properties.setProperty(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
        properties.setProperty(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);

        Client client = ((XFireProxy) Proxy.getInvocationHandler(service)).getClient();
        client.addOutHandler(new DOMOutHandler());
        client.addOutHandler(new WSS4JOutHandler(properties));

        // NOTE: WSHandlerConstants.USER and WSHandlerConstants.PW_CALLBACK_CLASS 
        // are not set here, but "per-call" in the WSSAcegiPropagationAdvice.
	}

	@Override
	public Object getObject() throws Exception {
		return myServiceProxy;
	}

	@Override
	public void setLookupServiceOnStartup(boolean lookupServiceOnStartup) {
		throw new RuntimeException("setLookupServiceOnStartup method deliberately not implemented here");
	}
}
