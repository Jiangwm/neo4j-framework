/*
 * Copyright (c) 2014 GraphAware
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
package com.graphaware.generator.relationship;

import com.graphaware.common.util.SameTypePair;
import com.graphaware.generator.distribution.DegreeDistribution;
import com.graphaware.generator.distribution.InvalidDistributionException;

import java.util.List;

/**
 * A component that generates relationships based on a given {@link DegreeDistribution}.
 */
public interface RelationshipGenerator {

    /**
     * Generate edges (relationships) based on a degree distribution.
     *
     * @param distribution of degrees.
     * @return pairs of node IDs representing edges.
     * @throws InvalidDistributionException in case the given distribution is invalid for the generator implementation.
     */
    List<SameTypePair<Integer>> generateEdges(DegreeDistribution distribution) throws InvalidDistributionException;
}
