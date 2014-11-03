package com.wide.wideweb.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
import com.wide.domainmodel.Exercise.SolutionType;
import com.wide.persistence.PersistenceListener;
import com.wide.service.WideService;
import com.wide.wideweb.beans.ExerciseBean;
import com.wide.wideweb.util.ViewDataCache;
import com.wide.wideweb.util.ViewUtils;
import com.wide.wideweb.views.components.VariableLayout;

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

    private List<VariableLayout> vars = new ArrayList<VariableLayout>();

    private int index = 0;

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

        final Button previewButton = new Button("Preview");
        this.editorLayout.addComponent(previewButton);

        final Label exerciseTextPreview = new Label("", ContentMode.HTML);
        exerciseTextPreview.setCaption("Exercise preview");
        this.editorLayout.addComponent(exerciseTextPreview);

        previewButton.addClickListener(new ClickListener() {

            /**
             * 
             */
            private static final long serialVersionUID = -4685275662657761127L;

            @Override
            public void buttonClick(ClickEvent event) {
                exerciseTextPreview.setValue(exerciseText.getValue());
                com.vaadin.ui.JavaScript.getCurrent().execute("MathJax.Hub.Queue([\"Typeset\",MathJax.Hub]);");
            }
        });

        final TextArea relatedLinks = new TextArea("Related links");
        this.editorLayout.addComponent(relatedLinks);
        this.form.bind(relatedLinks, "relatedLinks");
        relatedLinks.setNullRepresentation("");
        relatedLinks.setWidth("70%");

        final RichTextArea solutionText = new RichTextArea("Solution");
        this.editorLayout.addComponent(solutionText);
        this.form.bind(solutionText, "solutionText");
        solutionText.setNullRepresentation("");
        solutionText.setWidth("70%");

        final Button previewButtonS = new Button("Preview");
        this.editorLayout.addComponent(previewButtonS);

        final Label solutionTextPreview = new Label("", ContentMode.HTML);
        solutionTextPreview.setCaption("Solution preview");
        this.editorLayout.addComponent(solutionTextPreview);

        previewButtonS.addClickListener(new ClickListener() {

            /**
             * 
             */
            private static final long serialVersionUID = -4685275662657761127L;

            @Override
            public void buttonClick(ClickEvent event) {
                solutionTextPreview.setValue(solutionText.getValue());
                com.vaadin.ui.JavaScript.getCurrent().execute("MathJax.Hub.Queue([\"Typeset\",MathJax.Hub]);");
            }
        });

        final ComboBox types = new ComboBox("Solution type");
        types.setNullSelectionAllowed(false);
        types.addItem(SolutionType.SIMPLE);
        types.addItem(SolutionType.MULTI_CHOICE);
        types.setValue(SolutionType.SIMPLE);
        this.editorLayout.addComponent(types);
        this.form.bind(types, "type");

        HorizontalLayout var = getVar(false);
        var.setCaption("Short answer(s)");
        this.editorLayout.addComponent(var);
        this.index = this.editorLayout.getComponentIndex(var);

        final Button moreAnsw = new Button("Add more...");

        if (this.current.getVarName_2() != null && !this.current.getVarName_2().isEmpty()) {
            AbstractExerciseView.this.editorLayout.addComponent(getVar(true), AbstractExerciseView.this.index
                    + this.vars.size() - 1);
            if (this.vars.size() == 4) {
                moreAnsw.setEnabled(false);
            }
        }

        if (this.current.getVarName_3() != null && !this.current.getVarName_3().isEmpty()) {
            AbstractExerciseView.this.editorLayout.addComponent(getVar(true), AbstractExerciseView.this.index
                    + this.vars.size() - 1);
            if (this.vars.size() == 4) {
                moreAnsw.setEnabled(false);
            }
        }

        if (this.current.getVarName_4() != null && !this.current.getVarName_4().isEmpty()) {
            AbstractExerciseView.this.editorLayout.addComponent(getVar(true), AbstractExerciseView.this.index
                    + this.vars.size() - 1);
            if (this.vars.size() == 4) {
                moreAnsw.setEnabled(false);
            }
        }

        this.editorLayout.addComponent(moreAnsw);
        moreAnsw.addClickListener(new ClickListener() {

            /**
             * 
             */
            private static final long serialVersionUID = 378489649077395275L;

            @Override
            public void buttonClick(ClickEvent event) {
                if (AbstractExerciseView.this.vars.size() < 4) {
                    AbstractExerciseView.this.editorLayout.addComponent(getVar(true), AbstractExerciseView.this.index
                            + AbstractExerciseView.this.vars.size() - 1);
                    if (AbstractExerciseView.this.vars.size() == 4) {
                        moreAnsw.setEnabled(false);
                    }
                }
            }
        });

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

    public String cancelButtonLabel() {
        return "Go back";
    }

    private void rebindAll() {
        for (int i = 1; i < this.vars.size(); i++) {
            AbstractExerciseView.this.form.unbind(this.vars.get(i).getVarVal());
            AbstractExerciseView.this.form.unbind(this.vars.get(i).getVarName());
        }
        for (int i = 1; i < this.vars.size(); i++) {
            this.form.bind(this.vars.get(i).getVarName(), "varName_" + (i + 1));
            this.form.bind(this.vars.get(i).getVarVal(), "varVal_" + (i + 1));
        }
    }

    private HorizontalLayout getVar(boolean removable) {
        ClickListener removeAction = null;
        final VariableLayout layout = new VariableLayout(this.editorLayout);

        if (removable) {
            removeAction = new ClickListener() {

                /**
                 * 
                 */
                private static final long serialVersionUID = -8257009120769586300L;

                @Override
                public void buttonClick(ClickEvent event) {
                    AbstractExerciseView.this.editorLayout.removeComponent(layout);
                    switch (AbstractExerciseView.this.vars.indexOf(layout)) {
                        case 1:
                            AbstractExerciseView.this.current.setVarName_2(AbstractExerciseView.this.vars.size() > 2 ? AbstractExerciseView.this.vars.get(2)
                                    .getVarName().getValue() : "");
                            AbstractExerciseView.this.current.setVarVal_2(AbstractExerciseView.this.vars.size() > 2 ? AbstractExerciseView.this.vars.get(2)
                                    .getVarVal().getValue() : "");
                        case 2:
                            AbstractExerciseView.this.current.setVarName_3(AbstractExerciseView.this.vars.size() > 3 ? AbstractExerciseView.this.vars.get(3)
                                    .getVarName().getValue() : "");
                            AbstractExerciseView.this.current.setVarVal_3(AbstractExerciseView.this.vars.size() > 3 ? AbstractExerciseView.this.vars.get(3)
                                    .getVarVal().getValue() : "");
                        case 3:
                            AbstractExerciseView.this.current.setVarName_4("");
                            AbstractExerciseView.this.current.setVarVal_4("");

                    }
                    AbstractExerciseView.this.vars.remove(layout);
                    AbstractExerciseView.this.form.unbind(layout.getVarVal());
                    AbstractExerciseView.this.form.unbind(layout.getVarName());
                    rebindAll();
                    AbstractExerciseView.this.editorLayout.getComponent(AbstractExerciseView.this.index + AbstractExerciseView.this.vars.size())
                            .setEnabled(true);
                }
            };
            layout.addRemoveAction(removeAction);
        }

        this.form.bind(layout.getVarName(), "varName_" + (this.vars.size() + 1));
        this.form.bind(layout.getVarVal(), "varVal_" + (this.vars.size() + 1));

        this.vars.add(layout);

        return layout;
    }

    public abstract void actionButtonClicked(AbstractExerciseView view);

    public abstract String actionButtonLabel();

    public abstract boolean isViewProvideDelete();

    public abstract Button getDeleteButton();
}
