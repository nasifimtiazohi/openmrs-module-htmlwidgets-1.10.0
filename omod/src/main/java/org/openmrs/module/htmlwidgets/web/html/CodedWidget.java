package org.openmrs.module.htmlwidgets.web.html;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.openmrs.module.htmlwidgets.web.WidgetConfig;

/**
 * This represents a single widget on a form.
 */
public abstract class CodedWidget implements Widget {
	
	//******* PROPERTIES *************
	private List<Option> options;
	private String separator = "&nbsp;";
	
	//******* INSTANCE METHODS *************
	
	/** 
	 * @see Widget#render(WidgetConfig)
	 */
	public void render(WidgetConfig config, Writer w) throws IOException {

		int num = getOptions().size();
		for (int i=0; i<num; i++) {
			Option option = getOptions().get(i);
			WidgetConfig clonedConfig = config.clone();
			if (num > 1) {
				for (Attribute att : clonedConfig.getAttributes()) {
					if (att.getName().equalsIgnoreCase("id") && att.getValue() != null) {
						att.setFixedValue(att.getValue() + "_" + i);
					}
				}
			}
			clonedConfig.setConfiguredAttribute("value", option.getCode());
			if (isSelected(option, clonedConfig.getDefaultValue())) {
				clonedConfig.setConfiguredAttribute("checked", "true");
			}

			HtmlUtil.renderSimpleTag(w, "input", clonedConfig.getAttributes());
			w.write("&nbsp;"+option.getLabel());
			if ((i+1) < num) {
				w.write(getSeparator() == null ? "&nbsp;" : getSeparator());
			}
		}
	}
	
	/**
	 * Returns true if the passed Option is selected for the given value
	 * @param option - the Option to check
	 * @param value - the value to check against
	 */
	public boolean isSelected(Option option, Object value) {
		return ObjectUtils.equals(option.getValue(), value) ||
			ObjectUtils.equals(option.getCode(), value);
	}
	
	/**
	 * Returns the Selected option given the passed WidgetConfig
	 * @param config the WidgetConfig to check
	 * @return the matching Option
	 */
	public Option getSelectedOption(WidgetConfig config) {
		for (Option o : getOptions()) {
			if (isSelected(o, config.getDefaultValue())) {
				return o;
			}
		}
		return null;
	}
	
	/**
	 * Adds and configures the passed Option as needed with information in WidgetConfig
	 * @param config the WidgetConfig
	 */
	public void addOption(Option option, WidgetConfig config) {
		String prefix = StringUtils.isEmpty(option.getCode()) ? "empty" : option.getCode();
		String labelCode = config.getAttributeValue(prefix + "Code");
		String labelText = config.getAttributeValue(prefix + "Label");
		if (labelCode != null || labelText != null) {
			option.setLabelCode(labelCode);
			option.setLabelText(labelText);
		}
		getOptions().add(option);
	}
	
	public void sortOptions() {
		Collections.sort(getOptions(), new Comparator<Option>() {
			public int compare(Option o1, Option o2) {
				String group1 = o1.getGroup() == null ? "" : o1.getGroup().getLabel();
				String group2 = o2.getGroup() == null ? "" : o2.getGroup().getLabel();
				int ret = group1.compareToIgnoreCase(group2);
				if (ret == 0) {
					ret =  o1.getLabel().compareToIgnoreCase(o2.getLabel());
				}
				return ret;
			}
		});
	}
	
	//****** PROPERTY ACCESS *********
	
	/**
	 * @return the options
	 */
	public List<Option> getOptions() {
		if (options == null) {
			options = new ArrayList<Option>();
		}
		return options;
	}
	
	/**
	 * @param options the options to set
	 */
	public void setOptions(List<Option> options) {
		this.options = options;
	}

	/**
	 * @return the separator
	 */
	public String getSeparator() {
		return separator;
	}
	
	/**
	 * @param separator the separator to set
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}
}