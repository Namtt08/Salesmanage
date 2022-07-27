package org.project.manage.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.project.manage.entities.SystemSetting;
import org.project.manage.repository.SystemSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component(value = "systemConfig")
@Scope(value = "singleton")
public class SystemConfigUtil {

	 
	@Resource(name = "configureBean")
	private Properties configureBean;
	
	@Autowired
	private SystemSettingRepository systemSettingRepository;

	private Map<String, String> configMap = new HashMap<String, String>();

	public static final String LIST_PAGE_SIZE = "LIST_PAGE_SIZE";
	
	public static final String OTP_BLOCK = "OTP_BLOCK";
	
	public static final String POINT_LV = "POINT_LV";
	
	public static final int STATUS_ACTIVE = 1;
	
	public static final String PROMOTION = "PROMOTION";
	
	public static final String VOURCHER = "VOURCHER";
	
	public static final String discount = "1";
	
	public static final String percentage = "2";
	
	public static final String CASH = "CASH";
	
	public static final String WALLET = "WALLET";
	
	public static final String SEND_EMAIL = "SEND_EMAIL";
	
	public static final String MAIL_PRODUCT = "MAIL_PRODUCT";
	
	public static final String MAIL_ORDER = "MAIL_ORDER";
	
	@PostConstruct
	@Transactional
	public void init() {
		// Init system setting
		this.initSystemSetting();

	}

	@Transactional
	public void putConfig(String key, String value) {
		// Init system setting
		configMap.put(key, value);
	}

	public int getIntConfig(String key) {
		int result = 0;
		String value = this.getConfig(key);
		if (StringUtils.isNotEmpty(value)) {
			try {
				result = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				result = 0;
			}
		}
		return result;
	}

	public List<Integer> getListPageSize() {
		List<Integer> list = new ArrayList<Integer>();
		String listPageSize = getConfig(LIST_PAGE_SIZE);
		if (listPageSize != null) {
			String[] pages = listPageSize.split(",");
			if (pages.length > 0) {
				for (String i : pages) {
					list.add(Integer.parseInt(i));
				}
			}
		}
		return list;
	}

	public String getConfig(String key) {
		String configKey = key;
		String result = configMap.get(configKey);
		if (StringUtils.isBlank(result)) {
			result = configMap.get(key);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
    @Transactional
    public void initSystemSetting() {
        // Init System Setting
        List<SystemSetting> settings = systemSettingRepository.findAll();
        for (SystemSetting systemSetting : settings) {
            String key = systemSetting.getCode();
            String value = systemSetting.getValue();
            configMap.put(key, value);
        }
    }

}
