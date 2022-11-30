package com.ooooo.concurrent;

import java.util.concurrent.atomic.AtomicReference;

/**
 * CAS 实现 queue
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class LinkedQueue<E> {

  private final Node<E> dummy = new Node<>(null, null);

  private final AtomicReference<Node<E>> head = new AtomicReference<>(dummy);

  private final AtomicReference<Node<E>> tail = new AtomicReference<>(dummy);

  public boolean put(E item) {
    Node<E> newNode = new Node<>(item, null);
    while (true) {
      Node<E> curTail = tail.get();
      Node<E> tailNext = curTail.next.get();
      if (curTail == tail.get()) {
        if (tailNext != null) {
          // 队列处于中间状态，推进尾节点
          tail.compareAndSet(curTail, tailNext);
        } else {
          // 处于稳定状态，尝试插入新节点
          if (curTail.next.compareAndSet(null, newNode)) {
            // 插入操作成功，尝试推进尾节点
            tail.compareAndSet(curTail, newNode);
            return true;
          }
        }
      }
    }
  }

  public E take() {
    while (true) {
      if (head.get() == tail.get()) {
        return null;
      }

      Node<E> oldHead = head.get();
      Node<E> newHead = oldHead.next.get();
      if (newHead == null) {
        return null;
      }
      if (head.compareAndSet(oldHead, newHead)) {
        oldHead.next = null;
        return oldHead.item;
      }
    }
  }

  private static class Node<E> {

    private final E item;

    private AtomicReference<Node<E>> next;

    public Node(E item, Node<E> next) {
      this.item = item;
      this.next = new AtomicReference<>(next);
    }
  }
}
