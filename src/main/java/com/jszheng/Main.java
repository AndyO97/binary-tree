package com.jszheng;

import com.jszheng.base.completebt.CompleteBtManipulator;
import com.jszheng.base.simplebalanced.SimpleBalancedTreeManipulator;
import com.jszheng.heap.deap.DeapManipulator;
import com.jszheng.heap.maxheap.MaxHeapManipulator;
import com.jszheng.heap.minheap.MinHeapManipulator;
import com.jszheng.heap.minmaxheap.MinMaxHeapManipulator;
import com.jszheng.heap.smmh.SmmhManipulator;
import com.jszheng.manipulator.DataTypeManipulator;
import com.jszheng.manipulator.Manipulator;
import com.jszheng.manipulator.TreeManipulatorFactory;
import com.jszheng.searchtree.BstManipulator;
import com.jszheng.searchtree.avltree.AvlManipulator;
import com.jszheng.searchtree.rbtree.RbtManipulator;
import com.jszheng.searchtree.sbtree.SbtManipulator;
import com.jszheng.searchtree.splaytree.SplayTreeManipulator;
import com.jszheng.searchtree.treap.TreapManipulator;

import static com.jszheng.Env.*;

public class Main {

    public static void main(String[] args) {
        TreeManipulatorFactory factory = registerFactory();
        Manipulator manipulator = new DataTypeManipulator(factory);
        manipulator.executeWithPrompt();
    }

    private static TreeManipulatorFactory registerFactory() {
        TreeManipulatorFactory factory = new TreeManipulatorFactory();
        factory.register(COMPLETE_BINARY_TREE, CompleteBtManipulator.class);
        factory.register(SIMPLE_BALANCED_TREE, SimpleBalancedTreeManipulator.class);
        factory.register(BINARY_SEARCH_TREE, BstManipulator.class);
        factory.register(SPLAY_TREE, SplayTreeManipulator.class);
        factory.register(TREAP, TreapManipulator.class);
        factory.register(AVL_TREE, AvlManipulator.class);
        factory.register(RED_BLACK_TREE, RbtManipulator.class);
        factory.register(SIZE_BALACNED_TREE, SbtManipulator.class);
        factory.register(MAX_HEAP, MaxHeapManipulator.class);
        factory.register(MIN_HEAP, MinHeapManipulator.class);
        factory.register(MIN_MAX_HEAP, MinMaxHeapManipulator.class);
        factory.register(DEAP, DeapManipulator.class);
        factory.register(SMMH, SmmhManipulator.class);
        return factory;
    }
}
