/**
 *    Copyright ${license.git.copyrightYears} the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.internal;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.exception.ShellException;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class handles the task of merging changes into an existing XML file.
 * 
 * @author Jeff Butler
 */
public class XmlFileMergerJaxp {
    private static class NullEntityResolver implements EntityResolver {
        /**
         * returns an empty reader. This is done so that the parser doesn't
         * attempt to read a DTD. We don't need that support for the merge and
         * it can cause problems on systems that aren't Internet connected.
         */
        public InputSource resolveEntity(String publicId, String systemId)
                throws SAXException, IOException {

            StringReader sr = new StringReader(""); //$NON-NLS-1$

            return new InputSource(sr);
        }
    }

    /**
     * Utility class - no instances allowed
     */
    private XmlFileMergerJaxp() {
        super();
    }

    public static String getMergedSource(GeneratedXmlFile generatedXmlFile,
            File existingFile) throws ShellException {

        try {
            return getMergedSource(new InputSource(new StringReader(generatedXmlFile.getFormattedContent())),
                new InputSource(new InputStreamReader(new FileInputStream(existingFile), "UTF-8")), //$NON-NLS-1$
                existingFile.getName());
        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new ShellException(getString("Warning.13", //$NON-NLS-1$
                    existingFile.getName()), e);
        }
    }
    
    public static String getMergedSource(InputSource newFile,
            InputSource existingFile, String existingFileName) throws IOException, SAXException,
            ParserConfigurationException, ShellException {

        DocumentBuilderFactory factory = DocumentBuilderFactory
                .newInstance();
        factory.setExpandEntityReferences(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver(new NullEntityResolver());

        Document existingDocument = builder.parse(existingFile);
        Document newDocument = builder.parse(newFile);

        DocumentType newDocType = newDocument.getDoctype();
        DocumentType existingDocType = existingDocument.getDoctype();

        if (!newDocType.getName().equals(existingDocType.getName())) {
            throw new ShellException(getString("Warning.12", //$NON-NLS-1$
                    existingFileName));
        }

        Element existingRootElement = existingDocument.getDocumentElement();
        Element newRootElement = newDocument.getDocumentElement();

        // to check element attributes and child nodes , then update with the new elements'
        checkAndUpdateElement(existingRootElement, newRootElement);

        // reconcile the root element attributes -
        // take all attributes from the new element and add to the existing
        // element

//        // remove all attributes from the existing root element
//        NamedNodeMap attributes = existingRootElement.getAttributes();
//        int attributeCount = attributes.getLength();
//        for (int i = attributeCount - 1; i >= 0; i--) {
//            Node node = attributes.item(i);
//            existingRootElement.removeAttribute(node.getNodeName());
//        }
//
//        // add attributes from the new root node to the old root node
//        attributes = newRootElement.getAttributes();
//        attributeCount = attributes.getLength();
//        for (int i = 0; i < attributeCount; i++) {
//            Node node = attributes.item(i);
//            existingRootElement.setAttribute(node.getNodeName(), node
//                    .getNodeValue());
//        }
//
//        // remove the old generated elements and any
//        // white space before the old nodes
//        List<Node> nodesToDelete = new ArrayList<Node>();
//        NodeList children = existingRootElement.getChildNodes();
//        int length = children.getLength();
//        for (int i = 0; i < length; i++) {
//            Node node = children.item(i);
//            if (isGeneratedNode(node)) {
//                nodesToDelete.add(node);
//            } else if (isWhiteSpace(node)
//                    && isGeneratedNode(children.item(i + 1))) {
//                nodesToDelete.add(node);
//            }
//        }
//
//        for (Node node : nodesToDelete) {
//            existingRootElement.removeChild(node);
//        }
//
//        // add the new generated elements
//        children = newRootElement.getChildNodes();
//        length = children.getLength();
//        Node firstChild = existingRootElement.getFirstChild();
//        for (int i = 0; i < length; i++) {
//            Node node = children.item(i);
//            // don't add the last node if it is only white space
//            if (i == length - 1 && isWhiteSpace(node)) {
//                break;
//            }
//
//            Node newNode = existingDocument.importNode(node, true);
//            if (firstChild == null) {
//                existingRootElement.appendChild(newNode);
//            } else {
//                existingRootElement.insertBefore(newNode, firstChild);
//            }
//        }

        // pretty print the result
        return prettyPrint(existingDocument);
    }

    private static void checkAndUpdateElement(Element oldElement, Element newElement) throws ShellException {
        if (oldElement == null || newElement == null) {
            throw new ShellException("can not process element with null");
        }
        if (oldElement.getNodeType() != newElement.getNodeType()) {
            throw new ShellException("can not process element with different node type");
        }
        if (oldElement.getNodeType() == Node.ELEMENT_NODE) {
            // update with the newer attributes that both exists
            updateElementAttributes(oldElement, newElement);
            // update child nodes ,with these nodes ,they all have attribute that named "id" , otherwise we should not update them
            NodeList newChildren = newElement.getChildNodes();
            NodeList oldChildren = oldElement.getChildNodes();
            int length = newChildren.getLength();
            for (int i = 0; i < length; i++) {
                if (newChildren.item(i).getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                Element newChild = (Element) newChildren.item(i);
                Node attr = newChild.getAttributeNode("id");
                if (attr != null) {
                    Element oldChild = findElementByAttribute(attr, oldChildren);
                    if (oldChild == null) {
                        oldElement.appendChild(newChild);
                    } else {
                        updateNode(newChild, oldChild);
                    }
                }
            }
        } else {
            // it is not a element node , then update whole node with the newer
            updateNode(newElement, oldElement);
        }
    }

    private static Element findElementByAttribute(Node attr, NodeList nodeList) {
        int length = nodeList.getLength();
        for (int i = 0; i < length; i++) {
            if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element child = (Element) nodeList.item(i);
            String value = child.getAttribute(attr.getNodeName());
            if (value != null && value.equals(attr.getNodeValue())) {
                return child;
            }
        }
        return null;
    }

    private static void updateElementAttributes(Element oldElement, Element newElement) {
        NamedNodeMap attributes = newElement.getAttributes();
        int attributeCount = attributes.getLength();
        for (int i = attributeCount - 1; i >= 0; i--) {
            Node node = attributes.item(i);
            oldElement.setAttribute(node.getNodeName(), node.getNodeValue());
        }
    }

    private static void updateNode(Node newNode, Node oldNode) throws ShellException {
        if (oldNode.getParentNode() == null || newNode.getParentNode() == null) {
            throw new ShellException("can not replace root node");
        }
        Node parent = oldNode.getParentNode();
        Node tempNode = newNode.cloneNode(true);
        tempNode = parent.getOwnerDocument().adoptNode(tempNode);
        parent.replaceChild(tempNode, oldNode);
//        parent.removeChild(oldNode);
    }

    private static String prettyPrint(Document document) throws ShellException {
        DomWriter dw = new DomWriter();
        return dw.toString(document);
    }

    private static boolean isGeneratedNode(Node node) {
        boolean rc = false;

        if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String id = element.getAttribute("id"); //$NON-NLS-1$
            if (id != null) {
                for (String prefix : MergeConstants.OLD_XML_ELEMENT_PREFIXES) {
                    if (id.startsWith(prefix)) {
                        rc = true;
                        break;
                    }
                }
            }

            if (!rc) {
                // check for new node format - if the first non-whitespace node
                // is an XML comment, and the comment includes
                // one of the old element tags,
                // then it is a generated node
                NodeList children = node.getChildNodes();
                int length = children.getLength();
                for (int i = 0; i < length; i++) {
                    Node childNode = children.item(i);
                    if (isWhiteSpace(childNode)) {
                        continue;
                    } else if (childNode.getNodeType() == Node.COMMENT_NODE) {
                        Comment comment = (Comment) childNode;
                        String commentData = comment.getData();
                        for (String tag : MergeConstants.OLD_ELEMENT_TAGS) {
                            if (commentData.contains(tag)) {
                                rc = true;
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        }

        return rc;
    }

    private static boolean isWhiteSpace(Node node) {
        boolean rc = false;

        if (node != null && node.getNodeType() == Node.TEXT_NODE) {
            Text tn = (Text) node;
            if (tn.getData().trim().length() == 0) {
                rc = true;
            }
        }

        return rc;
    }
}
