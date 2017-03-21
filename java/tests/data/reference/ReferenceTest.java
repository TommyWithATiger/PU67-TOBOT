package data.reference;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNotSame;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;

import base.BaseTest;
import data.Topic;
import data.dao.ReferenceDAO;
import java.net.MalformedURLException;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;


public class ReferenceTest extends BaseTest {

  private static Reference reference;

  @Before
  public void populate() throws Exception{
    reference = new Reference("Title", "Description", "www.link.com", ReferenceType.WEBSITE);
    reference.create();
  }

  @Test
  public void testMakeReference() throws Exception{
    reference = new Reference("Title", "Description", "www.link.com", ReferenceType.WEBSITE);
  }

  @Test
  public void testGetSetTitle() throws Exception {
    assertEquals("Title", reference.getTitle());

    reference.setTitle("NewTitle");

    assertNotSame("Title", reference.getTitle());
    assertEquals("NewTitle", reference.getTitle());
  }

  @Test
  public void testGetSetDescription() throws Exception {
    assertEquals("Description", reference.getDescription());

    reference.setDescription("NewDescription");

    assertNotSame("Description", reference.getDescription());
    assertEquals("NewDescription", reference.getDescription());

  }

  @Test
  public void testGetSetType() throws Exception {
    assertEquals(ReferenceType.WEBSITE, reference.getReferenceType());

    reference.setReferenceType(ReferenceType.ARTICLE);

    assertNotSame(ReferenceType.WEBSITE, reference.getReferenceType());
    assertEquals(ReferenceType.ARTICLE, reference.getReferenceType());

  }

  @Test
  public void getSetLink() throws Exception {
    assertEquals("www.link.com", reference.getLink());
    reference.setLink("https://newlink.com");
    assertEquals("https://newlink.com", reference.getLink());
  }

  @Test
  public void getSetVideoLink() throws Exception {
    Reference videoRef = new Reference("T", "D", "www.youtube.com/watch?v=DFYRQ_zQ-gk", ReferenceType.VIDEO);
    assertEquals("www.youtube.com/watch?v=DFYRQ_zQ-gk", videoRef.getLink());
    videoRef.setLink("http://www.youtube.com/v/DFYRQ_zQ-gk?fs=1&hl=en_US");
    assertEquals("http://www.youtube.com/v/DFYRQ_zQ-gk?fs=1&hl=en_US", videoRef.getLink());
    videoRef.setLink("https://youtu.be/DFYRQ_zQ-gk");
    assertEquals("https://youtu.be/DFYRQ_zQ-gk", videoRef.getLink());
  }

  @Test(expected = MalformedURLException.class)
  public void badLink() throws Exception{
    reference.setLink("not a link");
  }

  @Test(expected = IllegalArgumentException.class)
  public void badVideoLink() throws Exception {
    Reference reference = new Reference("T", "D", "www.vimeo.com/videourl", ReferenceType.VIDEO);
  }

  @Test
  public void testGetID() throws Exception {
    assertEquals(1, reference.getId());
  }

  @Test
  public void addToRemoveFromTags() throws Exception {
    Topic topic = new Topic("Title", "Description");
    Topic topic2 = new Topic("Title", "Description");
    Topic topic3 = new Topic("Title", "Description");
    topic.create();
    topic2.create();
    topic3.create();

    reference.addTag(topic);
    assertTrue(reference.hasTag(topic));

    reference.removeTag(topic);
    assertFalse(reference.hasTag(topic));

    reference.addTags(Arrays.asList(topic, topic2, topic3));
    assertTrue(reference.hasTag(topic));
    assertTrue(reference.hasTag(topic2));
    assertTrue(reference.hasTag(topic3));
    assertEquals(3, reference.getTags().size());

    reference.removeTags(Arrays.asList(topic, topic2, topic3));
    assertFalse(reference.hasTag(topic));
    assertFalse(reference.hasTag(topic2));
    assertFalse(reference.hasTag(topic3));
    assertEquals(0, reference.getTags().size());
  }

  @Test
  public void testCreate() throws Exception {
    assertNotNull(ReferenceDAO.getInstance().findById(reference.getId()));
  }

  @Test
  public void testDelete() throws Exception {
    reference.delete();

    assertNull(ReferenceDAO.getInstance().findById(reference.getId()));
  }

  @Test
  public void testUpdate() throws Exception {
    reference.setTitle("New Title");
    reference.update();

    assertEquals("New Title", ReferenceDAO.getInstance().findById(reference.getId()).getTitle());
  }

  @Test
  public void testEquals() throws Exception {
    assertTrue(reference.equals(ReferenceDAO.getInstance().findById(reference.getId())));
  }

  @Test
  public void testHashCode() throws Exception {
    assertEquals(reference.getId(), reference.hashCode());

  }
}