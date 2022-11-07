package com.ooooo.juc;

import java.util.concurrent.atomic.AtomicReference;

/**
 * CAS 实现 stack
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class ConcurrentStack<E> {

  private final AtomicReference<Node<E>> top = new AtomicReference<>();


  public void push(E e) {
    Node<E> newHead = new Node<>(e);
    Node<E> oldHead;
    do {
      oldHead = top.get();
      newHead.next = oldHead;
    } while (!top.compareAndSet(oldHead, newHead));
  }

  public E pop() {
    Node<E> oldHead;
    Node<E> newHead;
    do {
      oldHead = top.get();
      if (oldHead == null) {
        return null;
      }

      newHead = oldHead.next;
    } while (!top.compareAndSet(oldHead, newHead));

    oldHead.next = null;
    return oldHead.e;
  }

  private static class Node<E> {

    private final E e;

    private Node<E> next;

    public Node(E e) {
      this.e = e;
    }
  }
}
