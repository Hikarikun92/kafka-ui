package br.hikarikun92.kafkaui.xml;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyTest {
    private Property property;
    private Property other;

    @Before
    public void setup() {
        property = new Property();
        other = new Property();
    }

    private void assertPropertyEquals() {
        assertEquals(property, other);
        assertEquals(other, property);
        assertEquals(property.hashCode(), other.hashCode());
    }

    private void assertPropertyNotEquals() {
        assertNotEquals(property, other);
        assertNotEquals(other, property);
    }

    @Test
    public void testEqualsSelf() {
        other = property;
        assertPropertyEquals();
    }

    @Test
    public void testEqualsNull() {
        assertNotEquals(null, property);
    }

    @Test
    public void testEqualsAnotherClass() {
        other = new Property() {
        };
        assertPropertyNotEquals();

        property.setName("name");
        property.setValue("value");

        other.setName(property.getName());
        other.setValue(property.getValue());
        assertPropertyNotEquals();
    }

    @Test
    public void testEqualsNullProperties() {
        assertPropertyEquals();
    }

    @Test
    public void testEqualsOneNullProperty() {
        property.setName("name");
        other.setName(property.getName());
        assertPropertyEquals();

        property.setName(null);
        other.setName(null);
        property.setValue("value");
        other.setValue(property.getValue());
        assertPropertyEquals();

        other.setName("name");
        assertPropertyNotEquals();

        property.setName(other.getName());
        property.setValue(null);
        assertPropertyNotEquals();
    }

    @Test
    public void testEqualsNotNullProperties() {
        property.setName("name 1");
        property.setValue("value 1");

        other.setName("name 2");
        other.setValue("value 2");
        assertPropertyNotEquals();

        other.setName(property.getName());
        assertPropertyNotEquals();

        other.setValue(property.getValue());
        assertPropertyEquals();
    }
}
