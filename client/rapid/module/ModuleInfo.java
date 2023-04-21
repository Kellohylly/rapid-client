package client.rapid.module;

import java.lang.annotation.*;

import client.rapid.module.modules.Category;
 
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {
	String getName();
	Category getCategory();
}
