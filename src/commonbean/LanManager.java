package commonbean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import model.SupportedLanguage;

@ManagedBean(name = "lanManager", eager = true)
@SessionScoped
public class LanManager implements Serializable{
	private static final long serialVersionUID = 6270353829156369762L;
	private String selectedLanguage;
	private ArrayList<SupportedLanguage> supportedLanguages;
	private HashMap<String, String> languagesCountryMap;

	@PostConstruct
	public void init(){
		try {
			this.setSelectedLanguage("en");
			this.supportedLanguages = new ArrayList<SupportedLanguage>();
			InputStream inputStream = LanManager.class.getResourceAsStream("/SupportedLanguages.properties");
			Properties properties = new Properties();
			properties.load(inputStream);
			for(Object key : Collections.list(properties.propertyNames())){
				supportedLanguages.add(new SupportedLanguage((String)key, (String)properties.get(key)));
			}

			this.languagesCountryMap = new HashMap<String, String>();
			inputStream = LanManager.class.getResourceAsStream("/LanguageCountryMap.properties");
			properties = new Properties();
			properties.load(inputStream);
			for(Object key: Collections.list(properties.propertyNames())){
				languagesCountryMap.put((String) key, (String) properties.get(key));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getText(String key){
		Locale locale = new Locale(selectedLanguage, languagesCountryMap.get(selectedLanguage));
		ResourceBundle messages;
		messages = ResourceBundle.getBundle("CommonsBundle", locale);
		return messages.getString(key);
	}

	public String getSelectedLanguage() {
		return selectedLanguage;
	}

	public void setSelectedLanguage(String selectedLanguage) {
		this.selectedLanguage = selectedLanguage;
	}

	public ArrayList<SupportedLanguage> getSupportedLanguages() {
		return supportedLanguages;
	}

	public void setSupportedLanguages(
			ArrayList<SupportedLanguage> supportedLanguages) {
		this.supportedLanguages = supportedLanguages;
	}

	public HashMap<String, String> getLanguagesCountryMap() {
		return languagesCountryMap;
	}

	public void setLanguagesCountryMap(HashMap<String, String> languagesCountryMap) {
		this.languagesCountryMap = languagesCountryMap;
	}
}
