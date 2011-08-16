/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mirth.
 *
 * The Initial Developer of the Original Code is
 * WebReach, Inc.
 * Portions created by the Initial Developer are Copyright (C) 2006
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *   Gerald Bortis <geraldb@webreachinc.com>
 *
 * ***** END LICENSE BLOCK ***** */

package com.webreach.mirth.server.controllers;


public abstract class TemplateController extends Controller {
    public static TemplateController getInstance() {
        return ControllerFactory.getFactory().createTemplateController();
    }
    
    /**
     * Adds a template with the specified id to the database. If a template with
     * the id already exists it will be overwritten.
     * 
     * @param id
     * @param template
     * @throws ControllerException
     */
    public abstract void putTemplate(String id, String template) throws ControllerException;

    /**
     * Returns the template with the specified id, null otherwise.
     * 
     * @param id
     * @return
     * @throws ControllerException
     */
    public abstract String getTemplate(String id) throws ControllerException;

    public abstract void clearTemplates() throws ControllerException;
}