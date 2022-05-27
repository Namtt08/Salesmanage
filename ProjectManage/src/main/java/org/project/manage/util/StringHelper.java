package org.project.manage.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class StringHelper {

	public static String randomString(int length, int leftLimitChar, int rightLimitChar) {
		int leftLimit = leftLimitChar <= 0 ? 97 : leftLimitChar; // letter 'a'
		int rightLimit = rightLimitChar <= 0 ? 122 : rightLimitChar; // letter 'z'
		int targetStringLength = length;
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		return buffer.toString();
	}

	public static String randomString(int length) {
		return randomString(length, 97, 122);
	}
	
	public static String randomStringForPassword() {
		String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
	    String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
	    String numbers = RandomStringUtils.randomNumeric(2);
//	    String specialChar = RandomStringUtils.random(3, 33, 47, false, false);
	    String totalChars = RandomStringUtils.randomAlphanumeric(3);
	    String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
	      .concat(numbers)
//	      .concat(specialChar)
	      .concat(totalChars);
	    List<Character> pwdChars = combinedChars.chars()
	      .mapToObj(c -> (char) c)
	      .collect(Collectors.toList());
	    Collections.shuffle(pwdChars);
	    String password = pwdChars.stream()
	      .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
	      .toString();
	    return password;
	}

	public static String randomDigits(int length) {
		return randomString(length, 48, 57);
	}

	public static String generateCuid(boolean isAdmin) {
		long timeStamp = new java.sql.Timestamp(System.currentTimeMillis()).getTime();
		if (isAdmin) {
			return "admin" + timeStamp + StringHelper.randomString(6);
		}
		return timeStamp + StringHelper.randomString(10);
	}

	public static String generateCif(boolean isAdmin) {
		if (isAdmin) {
			return "admin" + StringHelper.randomString(25);
		}
		return StringHelper.randomString(30);
	}

	public static String deAccent(String str) {
		String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
	}

	public static int monthsBetweenDates(Date startDate, Date endDate) {

		Calendar start = Calendar.getInstance();
		start.setTime(startDate);

		Calendar end = Calendar.getInstance();
		end.setTime(endDate);

		int monthsBetween = 0;
		int dateDiff = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH);

		if (dateDiff < 0) {
			int borrrow = end.getActualMaximum(Calendar.DAY_OF_MONTH);
			dateDiff = (end.get(Calendar.DAY_OF_MONTH) + borrrow) - start.get(Calendar.DAY_OF_MONTH);
			monthsBetween--;

			if (dateDiff > 0) {
				monthsBetween++;
			}
		} else {
			monthsBetween++;
		}
		monthsBetween += end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
		monthsBetween += (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12;
		return monthsBetween;
	}

	public static String convertNumber(double number) {
		Locale locale = LocaleContextHolder.getLocale().toLanguageTag().equals("en-US")
				|| LocaleContextHolder.getLocale().toLanguageTag().equals("vi") ? new Locale("vi", "VN") : Locale.US;
		NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
		String currency = formatter.format(number);
		return currency;
	}

	public static String formatMoney(int money) {
		String money_ = "";
		try {
			DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
			symbols.setDecimalSeparator(',');
			DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###", symbols);
			money_ = decimalFormat.format(money);
		} catch (Exception e) {
			log.error("formatMoney: "+ e.getMessage());
		}
		return money_;
	}

	public static String formatMoney(double money) {
		String money_ = "";
		try {
			DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
			symbols.setDecimalSeparator(',');
			DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###", symbols);
			money_ = decimalFormat.format(money);
		} catch (Exception e) {
			log.error("formatMoney: "+ e.getMessage());
		}
		return money_;
	}

	public static boolean isNumeric(String maybeNumeric) {
		return maybeNumeric != null && maybeNumeric.matches("[0-9]+");
	}

	public static String sha256(String token) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(token.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	
	public static String convertDate(Date date) {
		SimpleDateFormat simple = new SimpleDateFormat(AppConstants.DATE_FORMAT);
		return simple.format(date);
	}
	
	public static String getStringFromFile(String filePath) throws Exception {
	    File fl = new File(filePath);
	    FileInputStream fin = new FileInputStream(fl);
	    String ret = convertStreamToString(fin);
	    //Make sure you close all streams.
	    fin.close();
	    return ret;
	}
	
	public static String convertStreamToString(InputStream is) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	        sb.append(line).append("\n");
	    }
	    return sb.toString();
	}
	
	public static void writeJsonFile(File file, String json) {
	    BufferedWriter bufferedWriter = null;
	    try {

	        if (!file.exists()) {
	            System.out.println("file not exist");
	            file.createNewFile();
	        }

	        FileWriter fileWriter = new FileWriter(file);
	        bufferedWriter = new BufferedWriter(fileWriter);
	        bufferedWriter.write(json);

	    } catch (IOException e) {
	    	log.error("writeJsonFile: "+ e.getMessage());
	    } finally {
	        try {
	            if (bufferedWriter != null) {
	                bufferedWriter.close();
	            }
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    }
	}

	/**
	 * Checks if is null or empty.
	 *
	 * @param s
	 *            the s
	 *
	 * @return true, if is null or empty
	 */
	public static boolean isNullOrEmpty(final String s) {
		return (s == null || s.trim().length() == 0);
	}
	
	public static String convertObjectToString(Object input) {
		return input == null ? null : input.toString();

	}

	public static Long convertObjectToLong(Object input) {
		try {
			return Long.valueOf(input.toString());
		} catch (Exception e) {
			return null;
		}
	}

	public static Integer convertObjectToInt(Object input) {
		try {
			return Integer.valueOf(input.toString());
		} catch (Exception e) {
			return null;
		}
	}

	public static Double convertObjectToDouble(Object input) {
		try {
			return Double.valueOf(input.toString());
		} catch (Exception e) {
			return null;
		}
	}
}
