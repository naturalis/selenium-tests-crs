package nl.naturalis.selenium.crs.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class YamlReader {
	
	private static String yamlFile;
	
	public static void setFile(String file) {
		yamlFile = file;
	}

	static String readFile(String path, Charset encoding) {
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			return new String(encoded, encoding);
		} catch (IOException e) {
			return null;
		}
	}
	
	public Map getData() {
		String content = readFile(yamlFile, Charset.defaultCharset());
		Yaml yaml = new Yaml();
		Map map = (Map) yaml.load(content);
		return map;
	}
	

}
