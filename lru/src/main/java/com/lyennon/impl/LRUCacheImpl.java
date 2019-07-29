package com.lyennon.impl;

import com.lyennon.LRUCache;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yong.liang 2019/7/28
 */
public class LRUCacheImpl implements LRUCache {

    private int capacity;

    private Map<Integer, Node> map;

    private Node head;

    private Node tail;

    public LRUCacheImpl(int capacity) {
        this.capacity = capacity;
        map = new HashMap<Integer, Node>();
    }

    public int get(int key) {
        Node node = map.get(key);
        if (node == null) {
            return -1;
        } else {
            removeNode(node);
            offerNode(node);
            return node.value;
        }
    }

    private void offerNode(Node node) {
        if (tail != null) {
            tail.next = node;
        }
        node.previs = tail;
        node.next = null;
        tail = node;
        if (head == null) {
            head = node;
        }
    }

    private void removeNode(Node node) {
        if (node.previs != null) {
            node.previs.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.previs = node.previs;
        } else {
            tail = node.previs;
        }
    }

    public void put(int key, int value) {
        Node node = map.get(key);
        if (node == null) {
            if (map.size() >= capacity) {
                map.remove(head.key);
                removeNode(head);
            }
            Node newNode = new Node(key, value);
            offerNode(newNode);
            map.put(key, newNode);
        } else {
            node.value = value;
            removeNode(node);
            offerNode(node);
        }
    }

    static class Node {
        int key;
        int value;
        Node previs;
        Node next;

        public Node() {
        }

        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        LRUCacheImpl cache = new LRUCacheImpl(2);
        cache.put(1,1);
        cache.put(2,2);
        System.out.println(cache.get(1));
        cache.put(3,3);
        System.out.println(cache.get(2));
        cache.put(4,4);
        System.out.println(cache.get(1));
        System.out.println(cache.get(3));
        System.out.println(cache.get(4));
    }
}
