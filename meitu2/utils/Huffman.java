package com.example.meitu2.utils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

class HuffmanNode implements Comparable<HuffmanNode>{

    byte value;
    int frequency;

    HuffmanNode left;
    HuffmanNode right;

    public HuffmanNode(byte value,int frequency){
        this.value = value;
        this.frequency = frequency;
    }

    @Override
    public int compareTo(@NotNull HuffmanNode o) {
        return this.frequency - o.frequency;
    }
}
public class Huffman {

    public static Map<Byte,String> encodingTable;

    public static String huffmanEncoding(byte[] originalBytes){
        Map<Byte,Integer> frequencyMap = new HashMap<>();

        for (byte b: originalBytes) {
            frequencyMap.put(b, frequencyMap.getOrDefault(b,0)+1);
        }
        PriorityQueue<HuffmanNode> minHeap = new PriorityQueue<>();

        for (Map.Entry<Byte, Integer> entry : frequencyMap.entrySet()) {
            minHeap.add(new HuffmanNode(entry.getKey(),entry.getValue()));
        }
        while (minHeap.size()>1){
            HuffmanNode left = minHeap.poll();
            HuffmanNode right = minHeap.poll();

            HuffmanNode mergeNode = new HuffmanNode((byte)0, left.frequency + right.frequency);
            mergeNode.left = left;
            mergeNode.right = right;

            minHeap.add(mergeNode);
        }
        encodingTable = new HashMap<>();
        HuffmanNode root = minHeap.poll();

        buildEncodingTable(root,"",encodingTable);

        StringBuilder encodingData = new StringBuilder();
        for (Byte b: originalBytes) {
            encodingData.append(encodingTable.get(b));
        }
        System.out.println("原始数组长度"+originalBytes.length);
        System.out.println("哈夫曼后数组长度"+encodingData.length());
        return encodingData.toString();

    }

    public static void buildEncodingTable(HuffmanNode node,String currentCode,Map<Byte,String> encodingMap) {

        if (node == null) {
            return;
        }
        if (node.left == null && node.right == null) {
            encodingMap.put(node.value, currentCode);
        } else {
            buildEncodingTable(node.left, currentCode + "0", encodingMap);
            buildEncodingTable(node.right, currentCode + "1", encodingMap);
        }
    }
}
