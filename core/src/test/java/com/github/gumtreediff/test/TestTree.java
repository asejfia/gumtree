/*
 * This file is part of GumTree.
 *
 * GumTree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GumTree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GumTree.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011-2015 Jean-Rémy Falleri <jr.falleri@gmail.com>
 * Copyright 2011-2015 Floréal Morandat <florealm@gmail.com>
 */

package com.github.gumtreediff.test;

import java.util.List;

import com.github.gumtreediff.tree.TypeSet;
import com.github.gumtreediff.tree.Tree;
import org.junit.jupiter.api.Test;

import com.github.gumtreediff.tree.ITree;

import static org.junit.jupiter.api.Assertions.*;

public class TestTree {

    @Test
    public void testChildUrl() {
        ITree root = TreeLoader.getDummySrc();
        assertEquals("b", root.getChild("0").getLabel());
        assertEquals("c", root.getChild("0.0").getLabel());
        assertEquals("d", root.getChild("0.1").getLabel());
        assertEquals("e", root.getChild("1").getLabel());
    }

    @Test
    public void testGetParents() {
        ITree tree = TreeLoader.getDummySrc();
        ITree c = tree.getChild("0.0");
        assertEquals("c", c.getLabel());
        List<ITree> parents = c.getParents();
        assertEquals(2, parents.size());
        assertEquals("b", parents.get(0).getLabel());
        assertEquals("a", parents.get(1).getLabel());
        assertEquals(tree, parents.get(parents.size() - 1));
    }

    @Test
    public void testGetDescendants() {
        ITree tree = TreeLoader.getDummySrc();
        ITree b = tree.getChild(0);
        assertEquals("b", b.getLabel());
        List<ITree> descendants = b.getDescendants();
        assertEquals(2, descendants.size());
        assertEquals("c", descendants.get(0).getLabel());
        assertEquals("d", descendants.get(1).getLabel());
    }

    @Test
    public void testChildManipulation() {
        ITree t1 = new Tree(TypeSet.type("foo"));
        assertTrue(t1.isLeaf());
        assertTrue(t1.isRoot());
        assertEquals(0, t1.getChildren().size());
        ITree t2 = new Tree(TypeSet.type("foo"));
        t1.addChild(t2);
        assertFalse(t1.isLeaf());
        assertTrue(t1.isRoot());
        assertTrue(t2.isLeaf());
        assertEquals(t1, t2.getParent());
        ITree t3 = new Tree(TypeSet.type("foo"));
        t3.setParentAndUpdateChildren(t1);
        assertTrue(t3.isLeaf());
        assertEquals(t1, t3.getParent());
        assertEquals(2, t1.getChildren().size());
        assertEquals(t3, t1.getChild(1));
    }

    @Test
    public void testDeepCopy() {
        ITree root = TreeLoader.getDummySrc();
        ITree croot = root.deepCopy();
        assertTrue(root.isIsomorphicTo(croot));
        assertNotEquals(root, croot);
    }

    @Test
    public void testIsomophism() {
        ITree root = TreeLoader.getDummySrc();
        ITree rootCpy = TreeLoader.getDummySrc();
        assertTrue(root.isIsomorphicTo(rootCpy));
        rootCpy.getChild(0).getChild(0).setLabel("foo");
        assertFalse(root.isIsomorphicTo(rootCpy));
        root.getChild(0).getChild(0).setLabel("foo");
        assertTrue(root.isIsomorphicTo(rootCpy));
        rootCpy.addChild(new Tree(TypeSet.type("foo"), "toto"));
        assertFalse(root.isIsomorphicTo(rootCpy));
    }

    @Test
    public void testIsClone() {
        ITree tree = TreeLoader.getDummySrc();
        ITree copy = tree.deepCopy();
        assertTrue(tree.isIsomorphicTo(copy));
    }

}
