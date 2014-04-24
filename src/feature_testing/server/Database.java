package feature_testing.server;

import common.db.entity.Contact;
import common.db.entity.UserContact;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Database {

    public Database() {
        //new HibernateUtil();
    }

    public static void insertContact(Contact contactIn) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            //             String hql = "INSERT INTO Contact(firstName, lastName, salary)"
            //         + "SELECT firstName, lastName, salary FROM contactIn";


            String hql = "INSERT INTO Contact "
                    + "FROM contactIn";
            Query query = session.createQuery(hql);
            int result = query.executeUpdate();
            System.out.println("Rows affected: " + result);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public Integer insert(Object obj) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Integer objectID = null;
        try {
            tx = session.beginTransaction();

            objectID = (Integer) session.save(obj);
            session.flush();
            session.clear();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return objectID;
    }

    /* Method to  READ all the employees */
    public List<Object> select(Class className) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Object> objects = null;
        try {
            tx = session.beginTransaction();
            objects = session.createQuery("FROM " + className.getSimpleName()).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }


        return objects;
    }

    public void delete(Object obj) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(obj);
            session.flush();
            session.clear();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void update(Object obj) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(obj);
            session.flush();
            session.clear();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public Object get(Integer id, Class className) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Object object = null;
        try {
            tx = session.beginTransaction();

            object = session.get(className, id);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }


        return object;
    }

    public List<Integer> select(Class className, String string) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Integer> integers = null;
        try {
            tx = session.beginTransaction();
            integers = session.createQuery("FROM " + className.getSimpleName() + " " + string).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }


        return integers;
    }

    public void delete(Class<UserContact> aClass, String string) {
    }
}
