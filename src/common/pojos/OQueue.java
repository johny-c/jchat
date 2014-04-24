package common.pojos;

import java.util.Collection;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class OQueue extends Observable implements BlockingQueue<Object> {

    private ArrayBlockingQueue<Object> q;

    public OQueue() {
        q = new ArrayBlockingQueue<>(50);
    }

    public OQueue(Observer o) {
        this();
        this.addObserver(o);
    }

    public boolean offerDontNotify(Object e) {
        boolean b = q.offer(e);
        this.setChanged();
        return b;

    }

    @Override
    public boolean add(Object e) {
        return q.add(e);
    }

    @Override
    public boolean offer(Object e) {
        boolean b = q.offer(e);
        this.setChanged();
        this.notifyObservers();
        return b;
    }

    @Override
    public void put(Object e) throws InterruptedException {
        q.put(e);
    }

    @Override
    public boolean offer(Object e, long timeout, TimeUnit unit) throws InterruptedException {
        return q.offer(e, timeout, unit);
    }

    @Override
    public Object take() throws InterruptedException {
        return q.take();
    }

    @Override
    public Object poll(long timeout, TimeUnit unit) throws InterruptedException {
        return q.poll(timeout, unit);
    }

    @Override
    public int remainingCapacity() {
        return q.remainingCapacity();
    }

    @Override
    public boolean remove(Object o) {
        return q.remove(o);
    }

    @Override
    public boolean contains(Object o) {
        return q.contains(o);
    }

    @Override
    public int drainTo(Collection<? super Object> c) {
        return q.drainTo(c);
    }

    @Override
    public int drainTo(Collection<? super Object> c, int maxElements) {
        return q.drainTo(c, maxElements);
    }

    @Override
    public Object remove() {
        return q.remove();
    }

    @Override
    public Object poll() {
        return q.poll();
    }

    @Override
    public Object element() {
        return q.element();
    }

    @Override
    public Object peek() {
        return q.peek();
    }

    @Override
    public int size() {
        return q.size();
    }

    @Override
    public boolean isEmpty() {
        return q.isEmpty();
    }

    @Override
    public Iterator<Object> iterator() {
        return q.iterator();
    }

    @Override
    public Object[] toArray() {
        return q.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return q.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return q.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Object> c) {
        return q.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return q.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return q.retainAll(c);
    }

    @Override
    public void clear() {
        q.clear();
    }
}
