package data.reference;

import data.Topic;
import data.dao.ReferenceDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.apache.http.HttpException;
import org.json.JSONObject;

@Entity
@NamedQueries({
    @NamedQuery(name = "findAllReferences", query = "SELECT r FROM Reference r"),
    @NamedQuery(name = "findReferencesByTitle", query = "SELECT r FROM Reference r WHERE r.title LIKE CONCAT('%', :title, '%')"),
    @NamedQuery(name = "findReferencesByTag", query = "SELECT r FROM Reference r "
        + "JOIN Topic t WHERE t = :tag AND t MEMBER OF r.tags"),
})
@Table
public class Reference {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String title;
  private String description;
  private String link;

  @Enumerated(value = EnumType.STRING)
  @Convert(converter = ReferenceTypeConverter.class)
  @Column(name = "type")
  private ReferenceType referenceType;

  @ManyToMany
  @JoinTable(name = "REFERENCE_TAGS")
  private Collection<Topic> tags;

  public Reference() {
    super();
    tags = new ArrayList<>();
  }

  /***
   * Instantiates a Reference object
   *
   * @param title, the title/name of the reference
   * @param description, description of the reference
   * @param link, the URL link of the reference
   * @param referenceType, what kind of ReferenceType the reference is
   * @throws IOException, if it cannot read/write to socket
   * @throws HttpException, if it cannot connect to the URL
   * @throws IllegalArgumentException, if the URL is not properly formatted
   * @throws java.net.MalformedURLException, if the link string is not a URL
   */
  public Reference(String title, String description, String link, ReferenceType referenceType)
      throws IOException, HttpException {
    this();
    setTitle(title);
    setDescription(description);
    setReferenceType(referenceType);
    setLink(link);
  }

  /***
   * returns the id of the Reference
   *
   * @return int id
   */
  public int getId() {
    return id;
  }

  /***
   * gets the title of the Reference
   *
   * @return String title
   */
  public String getTitle() {
    return title;
  }

  /***
   * sets the title of the Reference
   *
   * @param title, String title to be set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /***
   * gets the description of the Reference
   *
   * @return String description
   */
  public String getDescription() {
    return description;
  }

  /***
   * sets the description of the Reference
   *
   * @param description, String description to be set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /***
   * gets the URL link of the Reference
   *
   * @return String link
   */
  public String getLink() {
    return link;
  }

  /***
   * sets the URL link of the Reference
   *
   * @param link, String link to be set
   * @throws IOException, if not able to read/write to socket
   * @throws IllegalArgumentException, if link is not properly formatted
   * @throws HttpException, if not able to connect/access the link
   * @throws java.net.MalformedURLException, if the String link is not a URL link
   */
  public void setLink(String link) throws IOException, IllegalArgumentException, HttpException {
    if (LinkValidation.validateLink(this, link)) {
      if (referenceType == ReferenceType.VIDEO) {
        String reg = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";
        Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(link);
        if (matcher.find()) {
          this.link = matcher.group(1);
        } else {
          throw new IllegalArgumentException("Could not get Youtube video ID");
        }
      } else if (!link.substring(0, 4).equals("http")) {
        this.link = "http://" + link;
      } else this.link = link;
    }
  }

  /***
   * gets the type of Reference this instance is
   *
   * @return ReferenceType referenceType
   */
  public ReferenceType getReferenceType() {
    return referenceType;
  }

  public void setReferenceType(ReferenceType referenceType)
      throws IllegalArgumentException, IOException, HttpException {
    if (this.referenceType != null && referenceType == ReferenceType.VIDEO && !LinkValidation.validateVideoLink(link)) {
      throw new IllegalArgumentException("Can't type to VIDEO, link is not from youtube");
    } else this.referenceType = referenceType;
  }

  /***
   * returns an unmodifiable copy of the tags this Reference has
   *
   * @return Collection<Topic> tags
   */
  public Collection<Topic> getTags() {
    return Collections.unmodifiableCollection(tags);
  }

  /***
   * returns True if the Reference is tagged with the tag
   *
   * @param topic, the tag to look for
   * @return boolean, True if tagged else False
   */
  public boolean hasTag(Topic topic) {
    return tags.contains(topic);
  }

  /***
   * adds a tag to the Reference
   *
   * @param tag, Topic to be added as a tag
   */
  public void addTag(Topic tag) {
    if (!tags.contains(tag)) {
      tags.add(tag);
    }
  }

  /***
   * adds multiple tags to the Reference
   *
   * @param tagList, Collection of topics to be added as tags
   */
  public void addTags(Collection<Topic> tagList) {
    tagList.forEach(this::addTag);
  }

  /***
   * removes a tag from the Reference if it is tagged
   *
   * @param tag, Topic to be removed as a tag
   */
  public void removeTag(Topic tag) {
    if (hasTag(tag)) {
      tags.remove(tag);
    }
  }

  /***
   * removes multiple tags from the Reference
   *
   * @param tagList, Collection of topics to be removed as tags
   */
  public void removeTags(Collection<Topic> tagList) {
    tagList.forEach(this::removeTag);
  }

  /**
   * Adds the Reference to the database
   */
  public void create() {
    ReferenceDAO.getInstance().persist(this);
  }

  /**
   * Removes the Reference from the database
   */
  public void delete() {
    ReferenceDAO.getInstance().remove(this);
  }

  /**
   * Updates the Reference's database entry
   */
  public void update() {
    ReferenceDAO.getInstance().merge(this);
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

  /**
   * Creates a JSON object with information about the reference
   *
   * @return A JSON object with the following data:
   *        id (int): the reference id
   *        title (String): the reference title
   *        description (String): the reference description
   *        link (String): the reference link
   *        type (String): the reference type
   */
  public JSONObject createAbout(){
    JSONObject aboutTopic = new JSONObject();
    aboutTopic.put("id", id);
    aboutTopic.put("title", title);
    aboutTopic.put("description", description);
    aboutTopic.put("link", link);
    aboutTopic.put("type", ReferenceTypeConverter.referenceTypeToString(referenceType));
    return aboutTopic;
  }

}
