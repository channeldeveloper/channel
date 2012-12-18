/*
 *  com.original.widget.model.PathSwitcherModel.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.model;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
/**
 * (Class Annotation.)
 *
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-7-6 22:03:37
 */
public class PathSwitcherModel {
    Tree<String> tree = new Tree<String>("root");

    public PathSwitcherModel(){
        
    }
    public PathSwitcherModel(List<String> paths){
        for(String path: paths){
            tree.addPath(fetchPaths(path), tree.getHead());
        }
    }
    public Collection<String> getFirstLevel(){
        return tree.getSuccessors("root");
    }
    public Collection<String> getChildren(String path){
        return tree.getSuccessors(path);
    }
    //增加一个路径
    public void addPath(String path){
        tree.addPath(fetchPaths(path), tree.getHead());
    }

    //获取路径下内西
    private Collection<String> fetchPaths(String path){
      String arr[] = path.split("_");
      if(arr.length!=3) return null;
      ArrayList<String> ret = new ArrayList<String>();
      String p = "";
      for(int i=0;i<arr.length;i++){
          if(p.isEmpty())
              p+=arr[i];
          else
              p+=("_"+arr[i]);
          ret.add(p);
      }
      return ret;
  }
 //内部数据类
 public class Tree<T> {

  private T head;

  private ArrayList<Tree<T>> leafs = new ArrayList<Tree<T>>();

  private Tree<T> parent = null;

  private HashMap<T, Tree<T>> locate = new HashMap<T, Tree<T>>();

  public Tree(T head) {
    this.head = head;
    locate.put(head, this);
  }

  public void addLeaf(T root, T leaf) {
    if (locate.containsKey(root)) {
      locate.get(root).addLeaf(leaf);
    } else {
      addLeaf(root).addLeaf(leaf);
    }
  }

  public Tree<T> addLeaf(T leaf) {
    Tree<T> t = new Tree<T>(leaf);
    leafs.add(t);
    t.parent = this;
    t.locate = this.locate;
    locate.put(leaf, t);
    return t;
  }

  public Tree<T> setAsParent(T parentRoot) {
    Tree<T> t = new Tree<T>(parentRoot);
    t.leafs.add(this);
    this.parent = t;
    t.locate = this.locate;
    t.locate.put(head, this);
    t.locate.put(parentRoot, t);
    return t;
  }

  public T getHead() {
    return head;
  }

  public Tree<T> getTree(T element) {
    return locate.get(element);
  }

  public Tree<T> getParent() {
    return parent;
  }

  public Collection<T> getSuccessors(T root) {
    Collection<T> successors = new ArrayList<T>();
    Tree<T> tree = getTree(root);
    if (null != tree) {
      for (Tree<T> leaf : tree.leafs) {
        successors.add(leaf.head);
      }
    }
    return successors;
  }

  public Collection<Tree<T>> getSubTrees() {
    return leafs;
  }

  @Override
  public String toString() {
    return printTree(0);
  }

  private static final int indent = 2;

  private String printTree(int increment) {
    String s = "";
    String inc = "";
    for (int i = 0; i < increment; ++i) {
      inc = inc + " ";
    }
    s = inc + head;
    for (Tree<T> child : leafs) {
      s += "\n" + child.printTree(increment + indent);
    }
    return s;
  }

  public void addPath(Collection<T> paths, T root){
      Iterator<T> iter = paths.iterator();
      T p = root;
      while(iter.hasNext()){
          T p1 = iter.next();
          if(getTree(p1)==null)
            addLeaf(p, p1);
          p= p1;
      }
  }
}
}
