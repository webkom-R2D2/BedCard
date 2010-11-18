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

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * A JAAS CallbackHandler Façade which delegates
 * to another CallbackHandler, retrieved from a ThreadLocal.
 * 
 * @author Michael Vorburger
 */
public class ThreadLocalCallbackHandler implements CallbackHandler {

	private static ThreadLocal<CallbackHandler> threadLocalCallbackHandler = new ThreadLocal<CallbackHandler>();

	public static void setThreadLocalCallbackHandler(CallbackHandler callbackHandler) {
		threadLocalCallbackHandler.set(callbackHandler);
	}

	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		threadLocalCallbackHandler.get().handle(callbacks);
	}
}
