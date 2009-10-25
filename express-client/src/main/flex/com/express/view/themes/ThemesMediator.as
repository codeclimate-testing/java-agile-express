package com.express.view.themes {
import com.express.ApplicationFacade;
import com.express.controller.event.GridButtonEvent;
import com.express.model.ProjectProxy;
import com.express.model.domain.Theme;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.controls.dataGridClasses.DataGridColumn;

import org.puremvc.as3.patterns.mediator.Mediator;

public class ThemesMediator extends Mediator{
   public static const NAME : String = "ThemesMediator";

   private var _proxy : ProjectProxy;

   public function ThemesMediator(viewComp : ThemesForm, name : String = NAME) {
      super(name, viewComp);
      _proxy = ProjectProxy(facade.retrieveProxy(ProjectProxy.NAME));
      viewComp.grdThemes.dataProvider = _proxy.themes;
      viewComp.colId.labelFunction = formatId;
      viewComp.btnAdd.addEventListener(MouseEvent.CLICK, handleAddTheme);
      viewComp.btnSave.addEventListener(MouseEvent.CLICK, handleSaveThemes);
      viewComp.btnCancel.addEventListener(MouseEvent.CLICK, handleCancel);
      viewComp.grdThemes.addEventListener(GridButtonEvent.CLICK, handleGridButton);
   }

   private function handleAddTheme(event : Event) : void {
      _proxy.themes.addItem(new Theme());
      var focusedCell:Object = new Object();;
      view.grdThemes.editedItemPosition = {rowIndex: _proxy.themes.length -1, columnIndex: 1};
   }

   private function handleSaveThemes(event : Event) : void {
      sendNotification(ApplicationFacade.NOTE_UPDATE_THEMES);
      closeWindow();
   }

   private function handleGridButton(event : GridButtonEvent) : void {
      var index: int = view.grdThemes.selectedIndex;
      _proxy.themes.removeItemAt(index);
   }

   public function handleCancel(event : MouseEvent) : void {
      _proxy.themes = _proxy.selectedProject.themes;
      closeWindow();
   }

   private function closeWindow() : void {
      view.parent.visible = false;
   }

   private function formatId(row : Object, col : DataGridColumn) : String {
      var theme : Theme = row as Theme;
      return theme.id ? theme.id.toString() : "-";
   }

   public function get view() : ThemesForm {
      return viewComponent as ThemesForm;
   }

}
}