/*
 * Copyright (c) 2013 GraphAware
 *
 * This file is part of GraphAware.
 *
 * GraphAware is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.graphaware.runtime;

import com.graphaware.runtime.config.RuntimeConfiguration;
import com.graphaware.runtime.manager.TxDrivenModuleManager;
import com.graphaware.runtime.module.RuntimeModule;
import com.graphaware.runtime.module.TxDrivenModule;
import com.graphaware.writer.DatabaseWriter;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;


/**
 * {@link com.graphaware.runtime.TxDrivenRuntime} backed by a {@link GraphDatabaseService}.
 * <p/>
 * Supports only {@link com.graphaware.runtime.module.TxDrivenModule} {@link com.graphaware.runtime.module.RuntimeModule}s as it might be backed by a {@link org.neo4j.unsafe.batchinsert.BatchGraphDatabaseImpl}.
 * <p/>
 * To use this {@link com.graphaware.runtime.GraphAwareRuntime}, please construct it using {@link com.graphaware.runtime.GraphAwareRuntimeFactory}.
 */
public class DatabaseRuntime extends TxDrivenRuntime<TxDrivenModule> {

    private final GraphDatabaseService database;
    private final TxDrivenModuleManager<TxDrivenModule> txDrivenModuleManager;
    private final DatabaseWriter databaseWriter;

    /**
     * Construct a new runtime. Protected, please use {@link com.graphaware.runtime.GraphAwareRuntimeFactory}.
     *
     * @param configuration         config.
     * @param database              on which the runtime operates.
     * @param txDrivenModuleManager manager for transaction-driven modules.
     * @param databaseWriter        to use when writing to the database.
     */
    protected DatabaseRuntime(RuntimeConfiguration configuration, GraphDatabaseService database, TxDrivenModuleManager<TxDrivenModule> txDrivenModuleManager, DatabaseWriter databaseWriter) {
        super(configuration);
        this.database = database;
        this.txDrivenModuleManager = txDrivenModuleManager;
        this.databaseWriter = databaseWriter;
        database.registerTransactionEventHandler(this);
        database.registerKernelEventHandler(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TxDrivenModuleManager<TxDrivenModule> getTxDrivenModuleManager() {
        return txDrivenModuleManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doRegisterModule(RuntimeModule module) {
        if (module instanceof TxDrivenModule) {
            txDrivenModuleManager.registerModule((TxDrivenModule) module);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Transaction startTransaction() {
        return database.beginTx();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void afterShutdown() {
        super.afterShutdown();
        afterShutdown(database);
    }

    /**
     * React to shutdown.
     *
     * @param database which has been shut down.
     */
    protected void afterShutdown(GraphDatabaseService database) {
        //for subclasses
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DatabaseWriter getDatabaseWriter() {
        return databaseWriter;
    }
}
