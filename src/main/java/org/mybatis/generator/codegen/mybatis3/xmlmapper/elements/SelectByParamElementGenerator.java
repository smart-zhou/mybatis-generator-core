package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * SelectByParamElementGenerator
 *
 * @author hzzjb
 * @date 2017/3/17
 */
public class SelectByParamElementGenerator extends AbstractXmlElementGenerator {

    private boolean isSimple;

    public SelectByParamElementGenerator(boolean isSimple) {
        this.isSimple = isSimple;
    }

    public void addElements(XmlElement parentElement) {
        // TODO: 2017/3/17 条件查询返回列表
    }
}
