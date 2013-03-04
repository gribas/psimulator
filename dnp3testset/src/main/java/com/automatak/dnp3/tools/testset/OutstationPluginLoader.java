/**
 * Copyright 2013 Automatak, LLC
 *
 * Licensed to Automatak, LLC under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Automatak, LLC licenses this file to you
 * under the GNU Affero General Public License Version 3.0 (the "License");
 * you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/agpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.automatak.dnp3.tools.testset;

import com.automatak.dnp3.tools.pluginapi.OutstationPluginFactory;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

class OutstationPluginLoader {

    List<OutstationPluginFactory> loadOutstationPlugins(PluginLoaderListener listener)
    {
        List<OutstationPluginFactory> factories = new LinkedList<OutstationPluginFactory>();

        File folder = new File("./plugins");
        File[] jars = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });

        if(jars != null) {
            for(int i=0; i< jars.length; ++i) {
                try {
                    FileInputStream stream = new FileInputStream(jars[i]);
                    JarInputStream jarStream = new JarInputStream(stream);
                    Manifest mf = jarStream.getManifest();
                    Attributes a = mf.getMainAttributes();
                    String outstationFactory = a.getValue("outstationPluginFactoryClass");
                    jarStream.close();
                    if(outstationFactory != null) {
                        URL url = new URL("jar", "","file:" + jars[i].getAbsolutePath()+"!/");
                        ClassLoader loader = URLClassLoader.newInstance(
                                new URL[]{url},
                                getClass().getClassLoader()
                        );
                        Class<?> clazz = Class.forName(outstationFactory, true, loader);
                        Object factory = clazz.newInstance();
                        factories.add((OutstationPluginFactory) factory);
                    }
                }
                catch(IllegalAccessException ex) {
                    listener.onException(ex);
                }
                catch(InstantiationException ex) {
                    listener.onException(ex);
                }
                catch(ClassNotFoundException ex) {
                    listener.onException(ex);
                }
                catch(FileNotFoundException ex) {
                    listener.onException(ex);
                }
                catch(IOException ex)
                {
                    listener.onException(ex);
                }
                listener.onProgressUpdate(i, jars.length);
            }
        }

        return factories;
    }



}
