package feature_testing.server;

import common.pojos.Message;
import java.util.Collection;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ServerMessageQueue extends Observable implements BlockingQueue<Message> {

    public ServerMessageQueue(Observer observer) {
        this.addObserver(observer);
    }

    @Override
    public Message poll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Message element() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Message peek() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Iterator<Message> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Message> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean add(Message e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean offer(Message e) {
        this.setChanged();
        this.notifyObservers(e);
        return false;
    }

    @Override
    public void put(Message e) throws InterruptedException {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean offer(Message e, long timeout, TimeUnit unit)
            throws InterruptedException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Message take() throws InterruptedException {
        return take();
    }

    @Override
    public Message poll(long timeout, TimeUnit unit)
            throws InterruptedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int remainingCapacity() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean remove(Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int drainTo(Collection<? super Message> c) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int drainTo(Collection<? super Message> c, int maxElements) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Message remove() {
        // TODO Auto-generated method stub
        return null;
    }
}
