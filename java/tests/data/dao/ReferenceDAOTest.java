package data.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import base.BaseTest;
import data.Topic;
import data.reference.Reference;
import data.reference.ReferenceType;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;


public class ReferenceDAOTest extends BaseTest {

  private static Reference reference1;
  private static Reference reference2;
  private static Reference reference3;

  @Before
  public void populate() throws Exception{
    reference1 = new Reference("Title 1", "Description 1", "www.link1.com", ReferenceType.WEBSITE);
    reference1.create();
    reference2 = new Reference("Title 2", "Description 2", "http://link2.com/abcd/file.png", ReferenceType.IMAGE);
    reference2.create();
    reference3 = new Reference("Title 3", "Description 3", "https://www.link1.com/dsjfdsij/file.html", ReferenceType.ARTRICLE);
    reference3.create();
  }

  @Test
  public void findAll() throws Exception {
    List<Reference> result = ReferenceDAO.getInstance().findAll();
    assertTrue(result.contains(reference1));
    assertTrue(result.contains(reference2));
    assertTrue(result.contains(reference3));
    assertEquals(3, result.size());
  }

  @Test
  public void findReferenceByTag() throws Exception {
    Topic tag1 = new Topic("Topic 1 title", "Topic 1 description");
    tag1.create();
    Topic tag2 = new Topic("Topic 2 title", "Topic 2 description");
    tag2.create();

    reference1.addTag(tag1);
    reference2.addTags(Arrays.asList(tag1, tag2));

    List<Reference> result1 = ReferenceDAO.getInstance().findReferenceByTag(tag2);
    List<Reference> result2 = ReferenceDAO.getInstance().findReferenceByTag(tag1);

    assertTrue(result1.contains(reference2));
    assertTrue(result2.contains(reference1));
    assertTrue(result2.contains(reference2));
    assertFalse(result1.contains(reference1));
    assertEquals(2, result2.size());
    assertEquals(1, result1.size());
  }

}