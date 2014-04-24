package common.pojos;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

public class EnumUserType<E extends Enum<E>> implements UserType {

    private Class<E> clazz = null;

    protected EnumUserType(Class<E> c) {
        this.clazz = c;
    }

    private static final int[] SQL_TYPES = {Types.VARCHAR};

    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    @Override
    public Class returnedClass() {
        return clazz;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] strings, SessionImplementor si, Object o) throws HibernateException, SQLException {
        String name = rs.getString(strings[0]);
        E result = null;
        if (!rs.wasNull()) {
            result = Enum.valueOf(clazz, name);
        }
        return result;
    }

    @Override
    public void nullSafeSet(PreparedStatement ps, Object o, int i, SessionImplementor si) throws HibernateException, SQLException {
        if (null == o) {
            ps.setNull(i, Types.VARCHAR);
        } else {
            ps.setString(i, ((Enum) o).name());
        }
    }

    /**
     *
     * @param value
     * @return
     * @throws HibernateException
     */
    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {

        return cached;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if (null == x || null == y) {
            return false;
        }
        return x.equals(y);
    }

}
