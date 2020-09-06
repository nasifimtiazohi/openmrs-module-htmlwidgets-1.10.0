/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.htmlwidgets.web.handler;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

import org.openmrs.annotation.Handler;
import org.openmrs.module.htmlwidgets.web.WidgetConfig;
import org.openmrs.module.htmlwidgets.web.html.RepeatingWidget;
import org.openmrs.module.htmlwidgets.web.html.WidgetFactory;

/**
 * FieldGenHandler for String Types
 */
@Handler(supports={Collection.class}, order=50)
public class CollectionHandler extends WidgetHandler {
	
	/** 
	 * @see WidgetHandler#render(WidgetConfig)
	 */
	@Override
	public void render(WidgetConfig config, Writer w) throws IOException {
		RepeatingWidget widget = WidgetFactory.getInstance(RepeatingWidget.class, config);
		widget.render(config, w);
	}
	
	/** 
	 * @see WidgetHandler#parse(String)
	 */
	@Override
	public Object parse(String input, Class<?> type) {
		throw new IllegalArgumentException("Not supported");
	}
}
