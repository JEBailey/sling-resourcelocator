package com.sas.sling.resource.query;

import com.sas.sling.resource.ResourceLocator;

import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import cz.jirutka.rsql.parser.ast.node.AbstractNode;
import cz.jirutka.rsql.parser.ast.node.ArgumentNode;

public class ArgumentVisitor implements RSQLVisitor<Object, ResourceLocator> {

	@Override
	public Object visit(AbstractNode node, ResourceLocator param) {
		return ((ArgumentNode)node).getValue();
	}

}
