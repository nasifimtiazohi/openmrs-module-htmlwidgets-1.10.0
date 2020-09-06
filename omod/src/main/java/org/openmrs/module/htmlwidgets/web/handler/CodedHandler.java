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

import org.apache.commons.lang.StringUtils;
import org.openmrs.module.htmlwidgets.web.WidgetConfig;
import org.openmrs.module.htmlwidgets.web.html.AjaxAutocompleteWidget;
import org.openmrs.module.htmlwidgets.web.html.AutocompleteWidget;
import org.openmrs.module.htmlwidgets.web.html.CodedWidget;
import org.openmrs.module.htmlwidgets.web.html.Option;
import org.openmrs.module.htmlwidgets.web.html.RadioWidget;
import org.openmrs.module.htmlwidgets.web.html.SelectWidget;
import org.openmrs.module.htmlwidgets.web.html.WidgetFactory;

/**
 * FieldGenHandler for Coded Types
 */
public abstract class CodedHandler extends WidgetHandler {
	
	/**
	 * Provides a means for specifying default configuration values
	 */
	protected void setDefaults(WidgetConfig config) {
		if (StringUtils.isEmpty(config.getFormat())) {
			config.setFormat("select");
		}
	}
	
	/** 
	 * @see WidgetHandler#render(WidgetConfig, Writer)
	 */
	@Override
	public void render(WidgetConfig config, Writer w) throws IOException {
		
		setDefaults(config);
		Class<? extends CodedWidget> t = SelectWidget.class;
		if ("select".equalsIgnoreCase(config.getFormat())) {
			t = SelectWidget.class;
		}
		else if ("radio".equalsIgnoreCase(config.getFormat())) {
			t = RadioWidget.class;
		}
		else if ("autocomplete".equalsIgnoreCase(config.getFormat())) {
			t = AutocompleteWidget.class;
		}
		else if ("ajax".equalsIgnoreCase(config.getFormat())) {
			if (StringUtils.isNotEmpty(config.getAttributeValue("ajaxUrl"))) {
				t = AjaxAutocompleteWidget.class;
			}
			else {
				t = AutocompleteWidget.class;
			}
		}
		CodedWidget widget = WidgetFactory.getInstance(t, config);
		
		String showEmptyAtt = config.getAttributeValue("showEmptyOption");
		if ("true".equals(showEmptyAtt) || (showEmptyAtt == null && widget instanceof SelectWidget)) {
			String emptyCode = config.getAttributeValue("emptyCode", null);
			String emptyLabel = config.getAttributeValue("emptyLabel", "");
			widget.addOption(new Option("", emptyLabel, emptyCode, null), config);
		}
		populateOptions(config, widget);
		String sortOptions = config.getAttributeValue("sortOptions", "true");
		if (sortOptions.equals("true")) {
			widget.sortOptions();
		}
		widget.render(config, w);
	}

	/**
	 * Subclasses should define the Coded Options here
	 */
	public abstract void populateOptions(WidgetConfig config, CodedWidget widget);
}
