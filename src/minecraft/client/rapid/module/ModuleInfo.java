package client.rapid.module;

import client.rapid.module.modules.Category;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {
	String getName();
	Category getCategory();
}
