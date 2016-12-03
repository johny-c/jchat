package client.db.util;

import client.networking.R;
import common.db.entity.AddContactRequest;
import common.db.entity.ChatMessage;
import common.db.entity.Conversation;
import common.db.entity.ConversationParticipant;
import common.db.entity.FileTransfer;
import common.db.entity.MyAccountRef;
import common.db.entity.Notification;
import common.db.entity.UserAccount;
import common.db.entity.UserContact;
import common.db.entity.UserIcon;
import common.db.entity.UserSession;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DB {

    static {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.close();
    }

    // Works
    public static Integer insert(Object obj) {
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
            R.log(e.toString());
        } finally {
            session.close();
        }
        return objectID;
    }

    /* Works, Method to READ all instances of some Entity */
    public static List<Object> select(Class className) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Object> objects = null;
        try {
            tx = session.beginTransaction();
            objects = session.createQuery("from " + className.getSimpleName()).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }

        return objects;
    }


    /* Works, Method to  READ all the contacts */
    public static List<UserAccount> getContacts(Integer userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Integer> ids = null;
        List<UserAccount> contacts = new ArrayList<>();
        try {
            tx = session.beginTransaction();
            ids = session.createQuery("select contactId from "
                    + UserContact.class.getSimpleName()
                    + " where userId = " + userId).list();
            for (Integer id : ids) {
                UserAccount uac = (UserAccount) session.get(UserAccount.class, id);
                contacts.add(uac);
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }

        return contacts;
    }

    public static UserIcon getUserIcon(Integer userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        UserIcon icon = null;
        try {
            tx = session.beginTransaction();
            String hql = "from "
                    + UserIcon.class.getSimpleName()
                    + " where uacId = " + userId;
            icon = (UserIcon) session.createQuery(hql).uniqueResult();

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }

        return icon;
    }

    public static List<Integer> getMyAccounts() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Integer id = null;
        List<Integer> accountIds = null;
        try {
            tx = session.beginTransaction();
            accountIds = session.createQuery("select accountId from " + MyAccountRef.class.getSimpleName()).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }

        return accountIds;
    }

    public static List<Notification> getOldNotifications() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Notification> notifs = null;
        try {
            tx = session.beginTransaction();
            String hql = "from "
                    + Notification.class.getSimpleName()
                    + " where status = :old";
            notifs = session.createQuery(hql).setParameter("old", Notification.Status.DELETED).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }

        return notifs;
    }

    public static void deleteOldSessions(Integer uacId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "delete from "
                    + UserSession.class.getSimpleName()
                    + " where userId = :uacId";
            session.createQuery(hql).setParameter("uacId", uacId).executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }
    }

    // not tested
    public static List<Conversation> getConversations() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Conversation> objects = null;
        try {
            tx = session.beginTransaction();
            objects = session.createQuery("from " + Conversation.class.getSimpleName()).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }

        return objects;
    }

// not tested
    public static AddContactRequest getACRsent(Integer recipientId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        AddContactRequest acr = null;
        try {
            tx = session.beginTransaction();
            String hql = "from " + AddContactRequest.class.getSimpleName() + " where recipientUserId = :rid";
            Query query = session.createQuery(hql);
            query.setParameter("rid", recipientId);
            query.setMaxResults(1);
            acr = (AddContactRequest) query.uniqueResult();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }
        return acr;
    }

// not tested Implement status unread
    public static List<ChatMessage> getUnreadChatMessages(Integer convId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<ChatMessage> objects = null;
        try {
            tx = session.beginTransaction();
            String hql = "from " + ChatMessage.class.getSimpleName() + " where conversationId = :convid";
            Query query = session.createQuery(hql);
            query.setParameter("convid", convId);
            objects = query.list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }

        return objects;
    }

    public static List<ConversationParticipant> getConvParticipants(Integer convId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<ConversationParticipant> parts = null;
        try {
            tx = session.beginTransaction();
            String hql = "from " + ConversationParticipant.class.getSimpleName() + " where conversationId = :convid";
            Query query = session.createQuery(hql);
            query.setParameter("convid", convId);
            parts = query.list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }

        return parts;
    }

    // not tested 
    public static List<FileTransfer> getFileTransfers(Integer convId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<FileTransfer> objects = null;
        try {
            tx = session.beginTransaction();
            String hql = "from " + FileTransfer.class.getSimpleName() + " where conversationId = :convid";
            Query query = session.createQuery(hql);
            query.setParameter("convid", convId);
            objects = query.list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }

        return objects;
    }

    // not tested
    public static FileTransfer getFileTransfer(Integer convId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        FileTransfer object = null;
        try {
            tx = session.beginTransaction();
            String hql = "from " + FileTransfer.class.getSimpleName() + " where conversationId = :convid";
            Query query = session.createQuery(hql);
            query.setParameter("convid", convId);
            object = (FileTransfer) query.uniqueResult();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }

        return object;

    }

    // Works
    public static void update(Object o) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(o);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }
    }

    // Works
    public static Object get(Integer id, Class className) {
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
            R.log(e.toString());
        } finally {
            session.close();
        }

        return object;
    }

    // Works
    public static void delete(Object obj) {
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
            R.log(e.toString());
        } finally {
            session.close();
        }
    }

    public static void deleteAccount(UserAccount uac) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Integer accountId = uac.getId();
        try {
            tx = session.beginTransaction();
            session.delete(uac);
            // delete MyAccountRef
            // delete UserAccounts (of contacts)
            // delete UserContact
            // delete UserSession
            // delete ACRs
            // delete ChatMessages
            // delete FileTransfers
            // delete Conversations    
            // delete Notifications
            session.flush();
            session.clear();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }

    }

    // Works, Delete all entries from a table
    public static void deleteAll(Class aClass) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "delete from " + aClass.getSimpleName() + " where 1=1";
            session.createQuery(hql).executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }
    }

    // Works
    public static Integer findFileIdByNotificationId(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Integer fileId = null;
        try {
            tx = session.beginTransaction();
            String hql = "select id from " + FileTransfer.class.getSimpleName() + " where notificationId = :nid";
            Query query = session.createQuery(hql);
            query.setParameter("nid", id);
            query.setMaxResults(1);
            fileId = (Integer) query.uniqueResult();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }
        return fileId;
    }

    public static UserSession getLastUserSession(Integer userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        UserSession object = null;
        try {
            tx = session.beginTransaction();
            String hql = "from " + UserSession.class.getSimpleName() + " where userId = :uacId";
            Query query = session.createQuery(hql);
            query.setParameter("uacId", userId);
            query.setMaxResults(1);
            object = (UserSession) query.uniqueResult();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }

        return object;
    }

    public static void deleteACRs(Integer userId, Integer contactId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "delete from "
                    + AddContactRequest.class.getSimpleName()
                    + " where questerUserId = :userId and "
                    + " recipientUserId = :contactId";
            Query q1 = session.createQuery(hql);
            q1.setParameter("userId", userId);
            q1.setParameter("contactId", contactId);
            q1.executeUpdate();
            Query q2 = session.createQuery(hql);
            q2.setParameter("userId", contactId);
            q2.setParameter("contactId", userId);
            q2.executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }
    }

    public static UserContact getUserContact(Integer userId, Integer ctId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        UserContact uc = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from "
                    + UserContact.class.getSimpleName()
                    + " where userId = :userId"
                    + " and contactId = :ctId");
            q.setParameter("userId", userId);
            q.setParameter("ctId", ctId);
            uc = (UserContact) q.uniqueResult();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }
        return uc;
    }

    /**
     *
     *
     *
     *
     *
     *
     * Methods that are too specific
     *
     *
     *
     *
     *
     */
    public static void updateNotificationStatus(Notification n) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "update " + Notification.class
                    .getSimpleName() + " set status = :status "
                    + "where id = :nid";
            Query query = session.createQuery(hql);

            query.setParameter(
                    "status", n.getStatus());
            query.setParameter(
                    "nid", n.getId());
            int result = query.executeUpdate();

            R.log(
                    "Rows affected: " + result);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }
    }

    public static void updateChatMessageSGID(ChatMessage cmIn) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "UPDATE " + ChatMessage.class
                    .getSimpleName() + " set serverGenId = :sgid "
                    + "WHERE clientGenId = :cgid";
            Query query = session.createQuery(hql);

            query.setParameter(
                    "sgid", cmIn.getServerGenId());
            query.setParameter(
                    "cgid", cmIn.getClientGenId());
            int result = query.executeUpdate();

            System.out.println(
                    "Rows affected: " + result);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }
    }

    public static ChatMessage findChatMessageByClientGenId(Integer clientGenId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        ChatMessage cm = null;
        try {
            tx = session.beginTransaction();
            String hql = "from " + ChatMessage.class
                    .getSimpleName() + " where clientGenId = :cgid";
            Query query = session.createQuery(hql);

            query.setParameter(
                    "cgid", clientGenId);
            query.setMaxResults(
                    1);
            cm = (ChatMessage) query.uniqueResult();

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }
        return cm;
    }

    public static Integer findConversationIdByNotificationId(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Integer convId = null;
        try {
            tx = session.beginTransaction();
            String hql = "from " + Conversation.class
                    .getSimpleName() + " where notificationId = :nid";
            Query query = session.createQuery(hql);

            query.setParameter(
                    "nid", id);
            query.setMaxResults(
                    1);
            convId = (Integer) query.uniqueResult();

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }
        return convId;
    }

    public static UserAccount findUserByUsername(String usernameInput) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        UserAccount user = null;
        try {
            tx = session.beginTransaction();
            String hql = "from " + UserAccount.class
                    .getSimpleName() + " u where u.username = :input";
            Query query = session.createQuery(hql);

            query.setParameter(
                    "input", usernameInput);
            R.log(
                    "Query: " + query.getQueryString());
            user = (UserAccount) query.uniqueResult();

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            R.log(e.toString());
        } finally {
            session.close();
        }
        return user;
    }

    public static void main(String[] args) {
        final String s1 = " I have opened a session. Totally opened, closed: ";
        final String s2 = " I have closed my session. Totally opened, closed: ";
        Runnable r = new Runnable() {

            @Override
            public void run() {
                String name = Thread.currentThread().getName();

                Session session = HibernateUtil.getSessionFactory().openSession();
                long openSessions = session.getSessionFactory().getStatistics().getSessionOpenCount();
                long closedSessions = session.getSessionFactory().getStatistics().getSessionCloseCount();
                System.out.println(name + s1 + openSessions + ", " + closedSessions);

                try {
                    Thread.sleep(10000);

                } catch (InterruptedException ex) {
                    Logger.getLogger(DB.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                session.close();
                openSessions = session.getSessionFactory().getStatistics().getSessionOpenCount();
                closedSessions = session.getSessionFactory().getStatistics().getSessionCloseCount();
                System.out.println(name + s2 + openSessions + ", " + closedSessions);
            }

        };

        new DB();
        HibernateUtil.getSessionFactory().getStatistics().setStatisticsEnabled(true);
        Thread t1 = new Thread(r);
        t1.start();

        try {
            Thread.sleep(2000);

        } catch (InterruptedException ex) {
            Logger.getLogger(DB.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        Thread t2 = new Thread(r);
        t2.start();

    }

}
