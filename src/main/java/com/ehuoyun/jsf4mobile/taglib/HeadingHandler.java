package com.ehuoyun.jsf4mobile.taglib;

import javax.faces.component.UIComponent;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.tag.jsf.ComponentConfig;
import com.sun.facelets.tag.jsf.ComponentHandler;

public class HeadingHandler extends ComponentHandler {

	public HeadingHandler(ComponentConfig config) {
		super(config);
	}

	@Override
	protected void onComponentCreated(FaceletContext ctx, UIComponent c,
			UIComponent parent) {
		super.onComponentCreated(ctx, c, parent);
		String level = getAttribute("level").getValue();
		level = level == null ? "1" : level;
		c.getAttributes().put("HEADING", level);
	}

}
