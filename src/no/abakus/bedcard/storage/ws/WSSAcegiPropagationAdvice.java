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

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Iterator;

import org.apache.ws.security.handler.WSHandlerConstants;
import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.client.XFireProxy;
import org.codehaus.xfire.handler.Handler;
import org.codehaus.xfire.security.wss4j.WSS4JOutHandler;
import org.springframework.aop.MethodBeforeAdvice;

/**
 * Integration between WSS4j and Acegi.
 *  
 * This Spring AOP Advice configures a WSS4JOutHandler on the target
 * (has to be previously configured) with the with uid+password 
 * found in Acegi's SecurityContextHolder.
 * 
 * @author Michael Vorburger
 */
public class WSSAcegiPropagationAdvice implements MethodBeforeAdvice {
	private String username;
	private String password;
	
	public WSSAcegiPropagationAdvice(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.aop.MethodBeforeAdvice#before(java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
	 */
	public void before(Method m, Object[] args, Object target) throws Throwable {	
		WSS4JOutHandler wssHandler = null;
        Client client = ((XFireProxy) Proxy.getInvocationHandler(target)).getClient();
        Iterator handlerIterator = client.getOutHandlers().iterator();
        while (handlerIterator.hasNext()) {
        	Handler aHandler = (Handler) handlerIterator.next();
			if (aHandler instanceof WSS4JOutHandler) {
				wssHandler = (WSS4JOutHandler) aHandler;
				break;
			}
		}
        if (wssHandler == null) {
        	throw new Exception("No WSS4JOutHandler configured for this service client");
        }
		
		if (username == null || password == null) {
			// If consumer code has not set any Authentication, there is nothing to propagate!
			wssHandler.setProperty(WSHandlerConstants.ACTION, WSHandlerConstants.NO_SECURITY);
		}
		else {
	        wssHandler.setProperty(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
			wssHandler.setProperty(WSHandlerConstants.USER, username);
	        ThreadLocalCallbackHandler.setThreadLocalCallbackHandler(new SinglePasswordCallbackHandler(username, password));
	        wssHandler.setProperty(WSHandlerConstants.PW_CALLBACK_CLASS, ThreadLocalCallbackHandler.class.getName());
	
	        // TODO This should also work, and is maybe better: properties.setProperty(WSHandlerConstants.PW_CALLBACK_REF, new SinglePasswordCallbackHandler(username, password));
	        // See Email "Re: [xfire-user] Password CallbackHandler wiring with WS-Security"
		}
	}
}
