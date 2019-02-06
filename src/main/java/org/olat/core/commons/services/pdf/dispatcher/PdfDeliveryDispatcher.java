/**
 * <a href="http://www.openolat.org">
 * OpenOLAT - Online Learning and Training</a><br>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); <br>
 * you may not use this file except in compliance with the License.<br>
 * You may obtain a copy of the License at the
 * <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache homepage</a>
 * <p>
 * Unless required by applicable law or agreed to in writing,<br>
 * software distributed under the License is distributed on an "AS IS" BASIS, <br>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
 * See the License for the specific language governing permissions and <br>
 * limitations under the License.
 * <p>
 * Initial code contributed and copyrighted by<br>
 * frentix GmbH, http://www.frentix.com
 * <p>
 */
package org.olat.core.commons.services.pdf.dispatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.olat.core.commons.services.pdf.PdfModule;
import org.olat.core.commons.services.pdf.PdfService;
import org.olat.core.commons.services.pdf.model.PdfDelivery;
import org.olat.core.dispatcher.Dispatcher;
import org.olat.core.dispatcher.DispatcherModule;
import org.olat.core.logging.OLog;
import org.olat.core.logging.Tracing;
import org.olat.core.util.FileUtils;
import org.olat.core.util.WebappHelper;
import org.olat.core.util.cache.CacheWrapper;
import org.olat.core.util.coordinate.CoordinatorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * Initial date: 6 févr. 2019<br>
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 *
 */
@Service("pdfDeliveryBean")
public class PdfDeliveryDispatcher implements Dispatcher {
	
	private static final OLog log = Tracing.createLoggerFor(PdfDeliveryDispatcher.class);
	
	@Autowired
	private PdfModule pdfModule;

	private final CacheWrapper<String,PdfDelivery> cache;
	
	@Autowired
	public PdfDeliveryDispatcher(CoordinatorManager coordinatorManager) {
		cache = coordinatorManager.getCoordinator().getCacher().getCache(PdfService.class.getSimpleName(), "delivery");
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		if(!pdfModule.isEnabled()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		
		String uriPrefix = DispatcherModule.getLegacyUriPrefix(request);
		final String origUri = request.getRequestURI();
		String uuid = origUri.substring(uriPrefix.length());
		int indexSuffix = uuid.indexOf('/');
		
		String key = null;
		String filename = null;
		if(indexSuffix > 0) {
			key = uuid.substring(0, indexSuffix);
			filename = uuid.substring(indexSuffix + 1);
		}
		
		PdfDelivery delivery = cache.get(key);
		if(delivery == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		File directory = new File(delivery.getDirectory());
		File file = new File(directory, filename);
		if(file.exists()) {
			String mimeType = WebappHelper.getMimeType(file.getName());
			response.setContentType(mimeType);
			response.setContentLengthLong(file.length());

			try(InputStream in = new FileInputStream(file)) {
				FileUtils.cpio(in, response.getOutputStream(), "static");
			} catch(Exception ex) {
				log.error("", ex);
			}
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
