package com.JinRui.fengkong4.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多叉树节点，用于表示征信报文的层次结构
 * 
 * @author JinRui
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode {
    
    /**
     * 节点名称（字段名或XML标签名）
     */
    private String nodeName;
    
    /**
     * 节点类型枚举
     */
    private NodeType nodeType;
    
    /**
     * 节点值（叶子节点的实际数据）
     */
    private Object value;
    
    /**
     * 数据类型
     */
    private String dataType;
    
    /**
     * 子节点列表
     */
    private List<TreeNode> children;
    
    /**
     * 父节点引用
     */
    @JsonIgnore
    private TreeNode parent;
    
    /**
     * 节点描述
     */
    private String description;
    
    /**
     * 树的层级深度
     */
    private Integer level;
    
    /**
     * 在同级节点中的索引
     */
    private Integer index;
    
    /**
     * 扩展属性
     */
    private Map<String, Object> attributes;
    
    /**
     * 是否为空节点
     */
    private Boolean isEmpty;
    
    /**
     * 节点类型枚举
     */
    public enum NodeType {
        ROOT,           // 根节点
        OBJECT,         // 对象节点
        ARRAY,          // 数组/列表节点
        ARRAY_ITEM,     // 数组元素节点
        LEAF            // 叶子节点
    }
    
    /**
     * 构造方法 - 创建叶子节点
     */
    public TreeNode(String nodeName, Object value, String dataType) {
        this.nodeName = nodeName;
        this.nodeType = NodeType.LEAF;
        this.value = value;
        this.dataType = dataType;
        this.children = new ArrayList<>();
        this.attributes = new HashMap<>();
        this.isEmpty = (value == null);
        this.level = 0;
    }
    
    /**
     * 构造方法 - 创建非叶子节点
     */
    public TreeNode(String nodeName, NodeType nodeType, String description) {
        this.nodeName = nodeName;
        this.nodeType = nodeType;
        this.description = description;
        this.children = new ArrayList<>();
        this.attributes = new HashMap<>();
        this.isEmpty = false;
        this.level = 0;
    }
    
    /**
     * 添加子节点
     */
    public void addChild(TreeNode child) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        child.setParent(this);
        child.setLevel(this.level + 1);
        child.setIndex(this.children.size());
        this.children.add(child);
    }
    
    /**
     * 移除子节点
     */
    public void removeChild(TreeNode child) {
        if (this.children != null) {
            this.children.remove(child);
            child.setParent(null);
            // 重新设置索引
            for (int i = 0; i < this.children.size(); i++) {
                this.children.get(i).setIndex(i);
            }
        }
    }
    
    /**
     * 判断是否为叶子节点
     */
    public boolean isLeaf() {
        return this.children == null || this.children.isEmpty();
    }
    
    /**
     * 根据名称查找子节点
     */
    public TreeNode getChildByName(String name) {
        if (this.children != null) {
            for (TreeNode child : this.children) {
                if (name.equals(child.getNodeName())) {
                    return child;
                }
            }
        }
        return null;
    }
    
    /**
     * 获取从根节点到当前节点的路径
     */
    public String getPath() {
        StringBuilder path = new StringBuilder();
        TreeNode current = this;
        List<String> pathList = new ArrayList<>();
        
        while (current != null) {
            pathList.add(0, current.getNodeName());
            current = current.getParent();
        }
        
        return String.join("/", pathList);
    }
    
    /**
     * 根据路径查找节点
     */
    public TreeNode findNodeByPath(String path) {
        if (path == null || path.isEmpty()) {
            return this;
        }
        
        String[] pathParts = path.split("/");
        TreeNode current = this;
        
        for (String part : pathParts) {
            if (part.isEmpty()) continue;
            current = current.getChildByName(part);
            if (current == null) {
                return null;
            }
        }
        
        return current;
    }
    
    /**
     * 获取节点总数（包括所有子节点）
     */
    public int getNodeCount() {
        int count = 1; // 当前节点
        if (this.children != null) {
            for (TreeNode child : this.children) {
                count += child.getNodeCount();
            }
        }
        return count;
    }
    
    /**
     * 获取树的最大深度
     */
    public int getDepth() {
        if (isLeaf()) {
            return 1;
        }
        
        int maxDepth = 0;
        if (this.children != null) {
            for (TreeNode child : this.children) {
                maxDepth = Math.max(maxDepth, child.getDepth());
            }
        }
        
        return maxDepth + 1;
    }
    
    /**
     * 初始化集合字段
     */
    public void initializeCollections() {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }
    }
}
