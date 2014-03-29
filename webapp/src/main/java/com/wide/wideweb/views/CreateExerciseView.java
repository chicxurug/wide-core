package com.wide.wideweb.views;

import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.context.annotation.Scope;

import ru.xpoft.vaadin.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.wide.domainmodel.Category;
import com.wide.wideweb.util.ViewDataCache;
import com.wide.wideweb.util.ViewUtils;

@org.springframework.stereotype.Component
@Scope("prototype")
@VaadinView(ViewUtils.CREATE_EXERCISE)
public class CreateExerciseView extends Panel implements View {

    private static final long serialVersionUID = -2022990984877322449L;

    private final ViewDataCache cache = ViewDataCache.getInstance();

    private Button createNewExerciseButton = new Button("Create");
    private FormLayout editorLayout = new FormLayout();

    protected void init() {
        setContent(this.editorLayout);

        NativeSelect language = new NativeSelect("Language");
        language.addItem("English");
        language.addItem("Hungarian");
        language.addItem("Other");
        this.editorLayout.addComponent(language);
        // language.setWidth("70%");
        language.setRequired(true);
        language.setValue("English");

        ComboBox category = new ComboBox("Category");
        initCategoriesComboBox(category);
        this.editorLayout.addComponent(category);
        // category.setWidth("70%");
        category.setRequired(true);
        category.setFilteringMode(FilteringMode.CONTAINS);
        category.setPageLength(20);

        TextField title = new TextField("Title");
        this.editorLayout.addComponent(title);
        title.setWidth("70%");
        title.setRequired(true);

        TextField uploader = new TextField("Uploaded by");
        this.editorLayout.addComponent(uploader);
        uploader.setEnabled(false);
        uploader.setValue(this.cache.getUsername());
        // uploader.setWidth("70%");

        TextField author = new TextField("Author");
        this.editorLayout.addComponent(author);
        author.setWidth("70%");

        TextField book = new TextField("Book");
        this.editorLayout.addComponent(book);
        book.setWidth("70%");

        TextField publisher = new TextField("Publisher");
        this.editorLayout.addComponent(publisher);
        publisher.setWidth("70%");

        OptionGroup difficulty = new OptionGroup("Difficulity level"); // TODO item captions
        difficulty.addItem("1 - Easy to solve");
        difficulty.addItem("2 - Average");
        difficulty.addItem("3 - Fair");
        difficulty.addItem("4 - Challenging");
        difficulty.addItem("5 - Hard to solve");
        this.editorLayout.addComponent(difficulty);
        // difficulty.setWidth("70%");
        difficulty.setRequired(true);

        OptionGroup schoolLevel = new OptionGroup("School level");
        schoolLevel.addItem("Elementary");
        schoolLevel.addItem("High School");
        schoolLevel.addItem("University");
        schoolLevel.addItem("Other");
        this.editorLayout.addComponent(schoolLevel);
        // schoolLevel.setWidth("70%");

        TextField tags = new TextField("Tags (separated by commas)");
        this.editorLayout.addComponent(tags);
        tags.setWidth("70%");

        RichTextArea exerciseText = new RichTextArea("Exercise");
        this.editorLayout.addComponent(exerciseText);
        exerciseText.setWidth("70%");
        exerciseText.setRequired(true);

        TextArea relatedLinks = new TextArea("Related links");
        this.editorLayout.addComponent(relatedLinks);
        relatedLinks.setWidth("70%");

        TextField shortAnswer = new TextField("Short answer");
        this.editorLayout.addComponent(shortAnswer);
        shortAnswer.setWidth("70%");
        shortAnswer.setRequired(true);

        RichTextArea solutionText = new RichTextArea("Solution");
        this.editorLayout.addComponent(solutionText);
        solutionText.setWidth("70%");

        this.editorLayout.addComponent(this.createNewExerciseButton);
    }

    private void initCategoriesComboBox(ComboBox categoryComboBox) {
        // Creating textual representation of categories
        SortedSet<String> categoryTexts = new TreeSet<String>();
        StringBuilder categoryText = new StringBuilder();
        for (Category category : this.cache.getCategories().values()) {
            categoryText.append(category.getName());
            while (category.getParent() != null && !category.getParent().equals(this.cache.getRootCategory())) {
                category = category.getParent();
                categoryText.insert(0, " / ");
                categoryText.insert(0, category.getName());
            }
            categoryTexts.add(categoryText.toString());
            categoryText.setLength(0);
        }
        // adding the categories to the combo box
        for (String categoryString : categoryTexts) {
            categoryComboBox.addItem(categoryString);
        }
    }

    @Override
    public void enter(ViewChangeEvent event) {
        init();
    }

}
