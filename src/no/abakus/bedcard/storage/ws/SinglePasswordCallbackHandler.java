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

import org.apache.ws.security.WSPasswordCallback;

/**
 * A JAAS CallbackHandler to handle the
 * org.apache.ws.security.WSPasswordCallback.
 * 
 * <p>It provides the username and password to WSS4j that have been set in the
 * constructor. This class is intended to be used on a per request scope.</p>.
 * 
 * @author Michael Vorburger
 */
public class SinglePasswordCallbackHandler implements CallbackHandler {

	private final String id;
	private final String pw;

	public SinglePasswordCallbackHandler(String id, String pw) {
		assert id != null;
		assert id.length() > 0;
		assert pw != null;
		assert pw.length() > 0;

		this.id = id;
		this.pw = pw;
	}

	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {

		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof WSPasswordCallback) {
				WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
				String requestedId = pc.getIdentifer();

				if (id.equals(requestedId)) {
					pc.setPassword(pw);
					
				} else {
					throw new IllegalArgumentException(
							"Something's wrong; constructed for '" + id
									+ "' but used with '" + requestedId + "'");
				}

			} else {
				throw new UnsupportedCallbackException(callbacks[i],
						"Unrecognized Callback");
			}
		}
	}
}
