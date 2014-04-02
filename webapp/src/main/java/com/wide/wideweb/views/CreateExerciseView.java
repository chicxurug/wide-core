package com.wide.wideweb.views;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import ru.xpoft.vaadin.VaadinView;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.wide.domainmodel.Category;
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.Exercise.DifficultyLevel;
import com.wide.domainmodel.Exercise.SchoolLevel;
import com.wide.persistence.PersistenceListener;
import com.wide.service.WideService;
import com.wide.wideweb.beans.ExerciseBean;
import com.wide.wideweb.util.ViewDataCache;
import com.wide.wideweb.util.ViewUtils;

@org.springframework.stereotype.Component
@Scope("prototype")
@VaadinView(ViewUtils.CREATE_EXERCISE)
public class CreateExerciseView extends Panel implements View {

    private static final long serialVersionUID = -2022990984877322449L;

    private static final Logger logger = LoggerFactory.getLogger(CreateExerciseView.class);

    private final WideService service = new WideService(PersistenceListener.getEntityManagerFactory());
    private final ViewDataCache cache = ViewDataCache.getInstance();

    // bean
    private ExerciseBean current = new ExerciseBean();
    // form for editing the bean
    private final BeanFieldGroup<ExerciseBean> form = new BeanFieldGroup<ExerciseBean>(ExerciseBean.class);

    private Button createNewExerciseButton = new Button("Create");
    private FormLayout editorLayout = new FormLayout();

    protected void init() {
        this.form.setItemDataSource(this.current);

        setContent(this.editorLayout);

        final NativeSelect language = new NativeSelect("Language");
        language.addItem("English");
        language.addItem("Hungarian");
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

        this.editorLayout.addComponent(this.createNewExerciseButton);
        this.editorLayout.addComponent(new Link("Go back", new ExternalResource("#" + ViewUtils.MAIN)));
        this.createNewExerciseButton.addClickListener(new ClickListener() {

            private static final long serialVersionUID = -5360404934227996076L;

            @Override
            public void buttonClick(ClickEvent event) {
                // check for invalid fields
                for (Field<?> field : CreateExerciseView.this.form.getFields()) {
                    try {
                        field.validate();
                        ((AbstractComponent) field).setComponentError(null);
                    } catch (InvalidValueException ex) {
                        Notification.show(ex.getMessage());
                        ((AbstractComponent) field).setComponentError(new UserError(ex.getMessage()));
                        return; // display only one error at a time
                    }
                }
                // if there is no general error, we try to commit the changes
                try {
                    CreateExerciseView.this.form.commit();
                    logger.info("Exercise created: {}", CreateExerciseView.this.current);
                    Exercise dbExercise = CreateExerciseView.this.current.convert(); // convert wrapper bean to DB entity
                    CreateExerciseView.this.service.saveOrUpdateExercise(dbExercise);
                    Notification.show("Exercise created! You are awesome!");
                    ViewDataCache.getInstance().doAllInit();
                } catch (CommitException e) {
                    logger.error("Error during comminting newly created exercise.", e);
                    Notification.show("Fatal error. :(");
                }
            }

        });
    }

    @Override
    public void enter(ViewChangeEvent event) {
        init();
    }

}
