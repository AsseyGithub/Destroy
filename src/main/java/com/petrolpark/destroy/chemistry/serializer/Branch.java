package com.petrolpark.destroy.chemistry.serializer;

import java.util.ArrayList;
import java.util.List;

import com.petrolpark.destroy.chemistry.legacy.LegacyAtom;
import com.petrolpark.destroy.chemistry.legacy.LegacyElement;
import com.petrolpark.destroy.chemistry.legacy.LegacyBond.BondType;

public class Branch {
    private List<Node> nodes = new ArrayList<>(); //list of Nodes in this Branch, not including Nodes in side Branches
    private Node startNode;
    private Node endNode;

    public Branch(Node node) {
        this.nodes.add(node);
        this.startNode = node;
        this.endNode = node;
    };

    public String serialize() {
        return startNode.serialize();
    };

    public Node getStartNode() {
        return startNode;
    };

    public Node getEndNode() {
        return endNode;
    };

    public List<Node> getNodes() {
        return nodes;
    };

    public Branch add(Node node, BondType bondType) {
        nodes.add(node);
        Edge newEdge = new Edge(endNode, node, bondType);
        endNode.addEdge(newEdge);
        node.addEdge(newEdge);
        node.setBranch(this);
        node.visited = true;
        endNode = node;
        return this;
    };

    /**
     * Connects the <em>start</em> of the given Branch to the <em>end</em> of this Branch.
     * Moves the end of this Branch to the end of the given Branch.
     * @return The original Branch with the new Branch added.
     */
    public Branch add(Branch branchToAdd, BondType bondType) {
        Edge newEdge = new Edge(endNode, branchToAdd.getStartNode(), bondType);
        nodes.addAll(branchToAdd.getNodes());
        for (Node node : branchToAdd.getNodes()) {
            node.setBranch(this);
        };
        branchToAdd.getStartNode().addEdge(newEdge);
        endNode.addEdge(newEdge);
        endNode = branchToAdd.endNode;
        return this;
    };

    public Branch flip() {
        for (Node node : nodes) {
            for (Edge edge : node.getEdges()) {
                if (!edge.marked) { //Mark so that edges are only flipped once
                    edge.flip();
                    edge.marked = true;
                };
            };
        };
        for (Node node : nodes) { //unmark again afterwards
            for (Edge edge : node.getEdges()) {
                edge.marked = false;
            };
        };
        Node temp = startNode;
        startNode = endNode;
        endNode = temp;
        return this;
    };

    public Float getMass() {
        float total = 0f;
        for (Node node : nodes) {
            total += getMassForComparisonInSerialization(node.getAtom());
            for (Branch branch : node.getSideBranches().keySet()) {
                total += branch.getMass();
            };
        };
        return total;
    };

    public Float getMassOfLongestChain() {
        float total = 0f;
        for (Node node : nodes) {
            total += getMassForComparisonInSerialization(node.getAtom());
        };
        return total;
    };

    public static Float getMassForComparisonInSerialization(LegacyAtom atom) {
        return atom.getElement().getMass() * (atom.getElement() == LegacyElement.R_GROUP ? atom.rGroupNumber : 1);
    };
};
