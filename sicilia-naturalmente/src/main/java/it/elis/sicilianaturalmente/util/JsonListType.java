package it.elis.sicilianaturalmente.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class JsonListType implements UserType, DynamicParameterizedType {

    private static final int[] SQL_TYPES = new int[]{Types.LONGVARCHAR};
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JavaType valueType = null;
    private Class<?> classType = null;

    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    @Override
    public Class<?> returnedClass() {
        return classType;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return Objects.hashCode(x);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {

    }

    /*@Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        return nullSafeGet(rs, names, owner);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        nullSafeSet(st, value, index);
    }

     */

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        String value = rs.getString(names[0]);
        Object result = null;
        if (valueType == null) {
            throw new HibernateException("Value type not set.");
        }
        if (value != null && !value.equals("")) {
            try {
                result = OBJECT_MAPPER.readValue(value, valueType);
            } catch (IOException e) {
                throw new HibernateException("Exception deserializing value " + value, e);
            }
        }
        return result;
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        StringWriter sw = new StringWriter();
        if (value == null) {
            st.setNull(index, Types.VARCHAR);
        } else {
            try {
                OBJECT_MAPPER.writeValue(sw, value);
                st.setString(index, sw.toString());
            } catch (IOException e) {
                throw new HibernateException("Exception serializing value " + value, e);
            }
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if (value == null) {
            return null;
        } else if (valueType.isCollectionLikeType()) {
            try {
                Object newValue = value.getClass().newInstance();
                Collection newValueCollection = (Collection) newValue;
                newValueCollection.addAll((Collection) value);
                return newValueCollection;
            } catch (InstantiationException e) {
                throw new HibernateException("Failed to deep copy the collection-like value object.", e);
            } catch (IllegalAccessException e) {
                throw new HibernateException("Failed to deep copy the collection-like value object.", e);
            }
        }

        return null;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) deepCopy(value);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }

    @Override
    public void setParameterValues(Properties parameters) {
        try {

            // Get entity class
            Class<?> entityClass = Class.forName(parameters.getProperty(DynamicParameterizedType.ENTITY));
            Field property = null;

            // Find the field
            while(property == null && entityClass != null){
                try {
                    property = entityClass.getDeclaredField(parameters.getProperty(DynamicParameterizedType.PROPERTY));
                } catch (NoSuchFieldException e) {
                    entityClass = entityClass.getSuperclass();
                }
            }

            if(property != null){
                ParameterizedType listType = (ParameterizedType) property.getGenericType();
                Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
                valueType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, listClass);
                classType = List.class;
            }

        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

}