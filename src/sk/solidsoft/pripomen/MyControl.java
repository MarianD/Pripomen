package sk.solidsoft.pripomen;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

/*
 * 2.3.2010:  Kódovanie pre InputStreamReader som dal natvrdo ako cp1250
 * 
 */


/**
 * Špecializovaná trieda typu ResourceBundle.Control, v ktorej je
 * však na naèítanie lokalizaèných súborov typu *.properties použitý 
 * Reader namiesto štandardného InputStreamu, aby slovenské znaky 
 * s diakritiko usprávne naèítal.
 * (InputStream predpokladá totiž kódovanie ISO-8859-1.)
 */
public final class MyControl extends ResourceBundle.Control {
	@Override
	 public ResourceBundle newBundle(String baseName,
	                                 Locale locale,
	                                 String format,
	                                 ClassLoader loader,
	                                 boolean reload)
	                  throws IllegalAccessException,
	                         InstantiationException,
	                         IOException {
	     if (baseName == null || locale == null	|| format == null || loader == null)
			throw new NullPointerException();
	     
	     ResourceBundle bundle = null;
	     
	     if (format.equals("java.properties")) {
			String 		bundleName   = toBundleName(baseName, locale);
			String 		resourceName = toResourceName(bundleName, "properties");
			InputStream stream 		 = null;
			InputStreamReader isr;
			
			if (reload) {
				URL url = loader.getResource(resourceName);
				if (url != null) {
					URLConnection connection = url.openConnection();
					if (connection != null) {
						// Disable caches to get fresh data for reloading.
						connection.setUseCaches(false);
						stream = connection.getInputStream();
					}
				}
			} else {
				stream = loader.getResourceAsStream(resourceName);
			}
			if (stream != null) {
			BufferedInputStream bis = new BufferedInputStream(stream);
				isr    = new InputStreamReader(bis, "utf8");
				bundle = new ReaderResourceBundle(isr);
				bis.close();
			}
			return bundle;
		} else {
			return super.newBundle(baseName, locale, format,
					loader, reload);
		}
	}
}


class ReaderResourceBundle extends ResourceBundle {
	private Properties props;

	ReaderResourceBundle(Reader rdr) throws IOException {
		props = new Properties();
		props.load(rdr);
	}

	@Override
	protected Object handleGetObject(String key) {
		return props.getProperty(key);
	}

	@Override
	public Enumeration<String> getKeys() {
		Vector<String> vector = new Vector<String>();
		for (Object s : props.keySet())
			vector.add((String) s);
		return vector.elements();
	}
}
