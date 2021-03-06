/*
 * Copyright (c) 2005, Henri Yandell
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or 
 * without modification, are permitted provided that the 
 * following conditions are met:
 * 
 * + Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer.
 * 
 * + Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 * 
 * + Neither the name of OSJava nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.osjava.scraping;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import com.generationjava.config.Config;
import org.osjava.oscube.container.Session;

import org.apache.commons.net.ftp.FTPClient;

public class FtpFetcher implements Fetcher {

    public Page fetch(String uri, Config cfg, Session session) throws FetchingException {
        try {

            FTPClient client = new FTPClient();
            URL url = new URL(uri);
            client.connect(url.getHost());

            String username = cfg.getString("username");
            String password = cfg.getString("password");
            if(username == null) {
                username = "anonymous";
            }
            if(password == null) {
                password = "";
            }

            client.login(username, password);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            client.retrieveFile( url.getPath(), baos );

	    // no way to find an ftp file type that I know of
            MemoryPage page = new MemoryPage(new String(baos.toByteArray()), null);
            // TODO: set page's document base
            return page;

        } catch(IOException ioe) {
            throw new FetchingException("Unable to download file. ", ioe);
        }
    }

}
