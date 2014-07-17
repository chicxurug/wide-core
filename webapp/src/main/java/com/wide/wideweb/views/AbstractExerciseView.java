package com.wide.wideweb.views;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.wide.domainmodel.Category;
import com.wide.domainmodel.Exercise.DifficultyLevel;
import com.wide.domainmodel.Exercise.SchoolLevel;
import com.wide.persistence.PersistenceListener;
import com.wide.service.WideService;
import com.wide.wideweb.beans.ExerciseBean;
import com.wide.wideweb.util.ViewDataCache;
import com.wide.wideweb.util.ViewUtils;

public abstract class AbstractExerciseView extends Panel implements View {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(AbstractExerciseView.class);

    protected final WideService service = new WideService(PersistenceListener.getEntityManagerFactory());
    protected final ViewDataCache cache = ViewDataCache.getInstance();

    // bean
    protected ExerciseBean current = new ExerciseBean();
    // form for editing the bean
    protected final BeanFieldGroup<ExerciseBean> form = new BeanFieldGroup<ExerciseBean>(ExerciseBean.class);

    private Button actionExerciseButton = new Button(actionButtonLabel());
    private FormLayout editorLayout = new FormLayout();

    protected void init() {
        this.form.setItemDataSource(this.current);

        setContent(this.editorLayout);

        final NativeSelect language = new NativeSelect("Language");
        language.addItem("English");
        language.addItem("Hungarian");
        language.addItem("Serbian");
        language.addItem("Other");
        this.editorLayout.addComponent(language);
        this.form.bind(language, "language");
        // language.setWidth("70%");
        language.setRequired(true);
        language.setValue("English");

        BeanItemContainer<Category> citems = new BeanItemContainer<Category>(Category.class, this.cache.getCategories().values());
        citems.sort(new Object[] { "path" }, new boolean[] { true });
        final ComboBox category = new ComboBox("Category", citems);
        this.editorLayout.addComponent(category);
        category.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        category.setItemCaptionPropertyId("path");
        this.form.bind(category, "category");
        // category.setWidth("70%");
        category.setNullSelectionAllowed(false);
        category.setRequired(true);
        category.setRequiredError("You have to choose a category.");
        category.setFilteringMode(FilteringMode.CONTAINS);
        category.setPageLength(20);
        category.setValidationVisible(false);

        final TextField newCategory = new TextField("Add a new category");
        this.editorLayout.addComponent(newCategory);
        newCategory.setWidth("70%");
        newCategory.setNullRepresentation("");
        newCategory.setRequired(false);
        newCategory.setRequiredError("The category's name cannot be empty.");
        newCategory.addValidator(new StringLengthValidator("The category's name should be at least 3 characters long.", 3, null, false));
        newCategory.setValidationVisible(false);
        Button addCategoryButton = new Button("Add");
        addCategoryButton.addClickListener(new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    // check for bad values
                    newCategory.validate();
                    // if everything is OK, we create the new category
                    Category parentCategory = (Category) category.getValue();
                    Category brandNewCategory = new Category(newCategory.getValue(), parentCategory);
                    // save it to the database
                    AbstractExerciseView.this.service.createCategory(brandNewCategory);
                    // init again to fill the "path" field
                    AbstractExerciseView.this.cache.initCategories();
                    // refresh "category" combobox
                    BeanItemContainer<Category> citems = new BeanItemContainer<Category>(Category.class, AbstractExerciseView.this.cache.getCategories()
                            .values());
                    citems.sort(new Object[] { "path" }, new boolean[] { true });
                    category.setContainerDataSource(citems);
                    category.markAsDirty();
                    // clear "new category" text field
                    newCategory.setValue("");
                    // set the new category as the selected one in the "category" combobox
                    brandNewCategory = AbstractExerciseView.this.cache.getCategoryById(brandNewCategory.getId());
                    category.setValue(brandNewCategory);
                    // notify the user about the new category
                    String caption = brandNewCategory.getPath();
                    logger.info("New category added: " + caption);
                    Notification.show("New category added: " + caption);
                } catch (InvalidValueException e) {
                    Notification.show(e.getMessage(), Type.ERROR_MESSAGE);
                }
            }

        });
        this.editorLayout.addComponent(addCategoryButton);

        final TextField title = new TextField("Title");
        this.editorLayout.addComponent(title);
        this.form.bind(title, "title");
        title.setWidth("70%");
        title.setNullRepresentation("");
        title.setRequired(true);
        title.setRequiredError("The title cannot be empty.");
        title.addValidator(new StringLengthValidator("The title should be at least 5 characters long.", 5, null, false));
        title.setValidationVisible(false);

        final TextField uploader = new TextField("Uploaded by");
        this.editorLayout.addComponent(uploader);
        this.form.bind(uploader, "uploader");
        // uploader.setWidth("70%");
        uploader.setNullRepresentation("");
        uploader.setEnabled(false);
        uploader.setValue(this.cache.getUsername());

        final TextField author = new TextField("Author");
        this.editorLayout.addComponent(author);
        this.form.bind(author, "author");
        author.setNullRepresentation("");
        author.setWidth("70%");

        final TextField book = new TextField("Book");
        this.editorLayout.addComponent(book);
        this.form.bind(book, "book");
        book.setNullRepresentation("");
        book.setWidth("70%");

        final TextField publisher = new TextField("Publisher");
        this.editorLayout.addComponent(publisher);
        this.form.bind(publisher, "publisher");
        publisher.setNullRepresentation("");
        publisher.setWidth("70%");

        final OptionGroup difficulty = new OptionGroup("Difficulity level");
        for (DifficultyLevel difficultyLevel : DifficultyLevel.values()) {
            difficulty.addItem(difficultyLevel);
        }
        this.editorLayout.addComponent(difficulty);
        this.form.bind(difficulty, "difficulty");
        difficulty.setNullSelectionAllowed(false);
        // difficulty.setWidth("70%");
        // difficulty.setValue("3 - Fair");
        difficulty.setRequired(true);
        difficulty.setRequiredError("The difficulty should be set.");
        // difficulty.addValidator(new StringLengthValidator("The title should be at least 5 characters long.", 5, null, false));
        // difficulty.setValidationVisible(false);

        BeanItemContainer<SchoolLevel> slitems = new BeanItemContainer<SchoolLevel>(SchoolLevel.class, Arrays.asList(SchoolLevel.values()));
        final OptionGroup schoolLevel = new OptionGroup("School level", slitems);
        schoolLevel.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        schoolLevel.setItemCaptionPropertyId("description");
        this.editorLayout.addComponent(schoolLevel);
        this.form.bind(schoolLevel, "schoolLevel");
        schoolLevel.setNullSelectionAllowed(true);
        // schoolLevel.setWidth("70%");

        final TextField tags = new TextField("Tags (separated by commas)");
        this.editorLayout.addComponent(tags);
        this.form.bind(tags, "tags");
        tags.setNullRepresentation("");
        tags.setWidth("70%");

        Link latexEditor = new Link("Open external latex formula editor", new ExternalResource("http://www.codecogs.com/latex/eqneditor.php"));
        latexEditor.setTargetName("_blank");
        this.editorLayout.addComponent(latexEditor);

        final RichTextArea exerciseText = new RichTextArea("Exercise");
        this.editorLayout.addComponent(exerciseText);
        this.form.bind(exerciseText, "exerciseText");
        exerciseText.setNullRepresentation("");
        exerciseText.setWidth("70%");
        exerciseText.setRequired(true);
        exerciseText.setRequiredError("The text of the exercise cannot be empty.");
        exerciseText.addValidator(new StringLengthValidator("The text should be at least 5 characters long.", 5, null, false));
        exerciseText.setValidationVisible(false);

        final TextArea relatedLinks = new TextArea("Related links");
        this.editorLayout.addComponent(relatedLinks);
        this.form.bind(relatedLinks, "relatedLinks");
        relatedLinks.setNullRepresentation("");
        relatedLinks.setWidth("70%");

        final TextField shortAnswer = new TextField("Short answer");
        this.editorLayout.addComponent(shortAnswer);
        this.form.bind(shortAnswer, "shortAnswer");
        shortAnswer.setNullRepresentation("");
        shortAnswer.setWidth("70%");
        shortAnswer.setRequired(true);
        shortAnswer.setRequiredError("The short answer cannot be empty.");
        // shortAnswer.addValidator(new StringLengthValidator("The short answer should be at least 1 characters long.", 1, null, false));
        // shortAnswer.setValidationVisible(false);

        final RichTextArea solutionText = new RichTextArea("Solution");
        this.editorLayout.addComponent(solutionText);
        this.form.bind(solutionText, "solutionText");
        solutionText.setNullRepresentation("");
        solutionText.setWidth("70%");
        // solutionText.setRequired(true);
        // solutionText.setRequiredError("The solution text of the exercise cannot be empty.");
        // solutionText.addValidator(new StringLengthValidator("The solution text should be at least 5 characters long.", 5, null, true));
        // solutionText.setValidationVisible(false);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponent(this.actionExerciseButton);
        if (isViewProvideDelete()) {
            buttonLayout.addComponent(getDeleteButton());
        }

        this.editorLayout.addComponent(buttonLayout);
        this.editorLayout.addComponent(new Link(cancelButtonLabel(), new ExternalResource("#!" + ViewUtils.MAIN)));
        this.actionExerciseButton.addClickListener(new ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                actionButtonClicked(AbstractExerciseView.this);
            }

        });
    }

    public abstract void actionButtonClicked(AbstractExerciseView view);

    public abstract String actionButtonLabel();

    public abstract boolean isViewProvideDelete();

    public abstract Button getDeleteButton();

    public String cancelButtonLabel() {
        return "Go back";
    }

}
