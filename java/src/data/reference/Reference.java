package data.reference;

import data.Topic;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Collections;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import org.apache.commons.validator.routines.UrlValidator;

public class Reference {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private String title;
  private String description;
  private String link;
  private ReferenceType referenceType;

  @ManyToMany
  private Collection<Topic> tags;

  public Reference() {
    super();
  }

  public Reference(String title, String description, String link, ReferenceType referenceType)
      throws MalformedURLException {
    this();
    setTitle(title);
    setDescription(description);
    setReferenceType(referenceType);
    setLink(link);
  }

  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) throws MalformedURLException {
    //if (link.matches("(https?:\\/\\/(?:www\\.|(?!www))[^\\s\\.]+\\.[^\\s]{2,}|www\\.[^\\s]+\\.[^\\s]{2,})\n")) {
    if (UrlValidator.getInstance().isValid(link)) {
      this.link = link;
    }
    else throw new MalformedURLException();
    //TODO different validation based on what type of link it is (youtube etc.)
  }

  public ReferenceType getReferenceType() {
    return referenceType;
  }

  public void setReferenceType(ReferenceType referenceType) {
    this.referenceType = referenceType;
  }

  /***
   *
   * @return unmodifiable copy of the tag collection
   */
  public Collection<Topic> getTags() {
    return Collections.unmodifiableCollection(tags);
  }

  public boolean hasTag(Topic topic) {
    return tags.contains(topic);
  }

  public void addTag(Topic tag) {
    if (!tags.contains(tag)) {
      tags.add(tag);
    }
  }

  public void addTags(Collection<Topic> newTags) {
    newTags.forEach(this::addTag);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Reference reference = (Reference) o;

    return id == reference.id;
  }

  @Override
  public int hashCode() {
    return id;
  }
}
