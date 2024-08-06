package com.petrolpark.destroy.chemistry.legacy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.petrolpark.destroy.chemistry.legacy.LegacyBond.BondType;

/**
 * A class whose purpose is to locate {@link LegacyFunctionalGroup functional Groups} in {@link Molecules}.
 * Group Finders must be instantiated during Mod setup.
 */
public abstract class GroupFinder {

    /**
     * All Group Finders known to Destroy.
     */
    private static Set<GroupFinder> FINDERS = new HashSet<>();

    public GroupFinder() {
        FINDERS.add(this);
    };

    /**
     * All Group Finders known to Destroy.
     * <p>This is called by a {@link LegacySpecies Molecule} when identifying which {@link LegacyFunctionalGroup} Groups a Molecule contains.
     * This should be no need to call this otherwise.</p>
     */
    public static Set<GroupFinder> allGroupFinders() {
        return FINDERS;
    };

    /**
     * Given a structure, this function should return all {@link LegacyFunctionalGroup functional Groups} that the structure contains.
     * For more information, see the <a href="https://github.com/petrolpark/Destroy/wiki/">Destroy Wiki</a>.
     * @param structure A Map of {@link LegacyAtom Atoms} to all {@link LegacyBond Bonds} that Atom has (see the {@code structure} property of {@link LegacyMolecularStructure})
     * @return The list of Groups which this Group Finder has identified as being contained within the given structure
     */
    public abstract List<LegacyFunctionalGroup<?>> findGroups(Map<LegacyAtom, List<LegacyBond>> structure);

    /**
     * A convenience method that gives all {@link LegacyAtom Atoms} of the given {@link LegacyElement} {@link Bonded bonded} (with any {@link LegacyBond.BondType type}) to the given Atom in the given structure.
     * @param structure A Map of Atoms in a {@link LegacySpecies} to all Bonds that Atom has (see the {@code structure} property of {@link LegacyMolecularStructure})
     * @param atom The Atom to which to check for Bonds
     * @param element The Element to check for
     * @see GroupFinder#bondedAtomsOfElementTo(Map, LegacyAtom, LegacyElement, BondType) Finding Atoms bonded with a specific type
     */
    public static List<LegacyAtom> bondedAtomsOfElementTo(Map<LegacyAtom, List<LegacyBond>> structure, LegacyAtom atom, LegacyElement element) {
        List<LegacyAtom> atoms = new ArrayList<>();
        for (LegacyBond bond : structure.get(atom)) {
            LegacyAtom destAtom = bond.getDestinationAtom();
            if (destAtom.getElement() == element && structure.containsKey(destAtom)) {
                atoms.add(destAtom);
            };
        };
        return atoms;
    };

    /**
     * A convenience method that gives all {@link LegacyAtom Atoms} of the given {@link LegacyElement} {@link Bonded bonded} (with the given {@link LegacyBond.BondType type}) to the given Atom in the given structure.
     * @param structure A Map of Atoms in a {@link LegacySpecies} to all Bonds that Atom has (see the {@code structure} property of {@link LegacyMolecularStructure})
     * @param atom The Atom to which to check for Bonds
     * @param element The Element to check for
     * @param bondType The type of Bond to check for
     * @see GroupFinder#bondedAtomsOfElementTo(Map, LegacyAtom, LegacyElement) Finding Atoms bonded with any type
     */
    public static List<LegacyAtom> bondedAtomsOfElementTo(Map<LegacyAtom, List<LegacyBond>> structure, LegacyAtom atom, LegacyElement element, BondType bondType) {
        List<LegacyAtom> atoms = new ArrayList<>();
        for (LegacyBond bond : structure.get(atom)) {
            LegacyAtom destAtom = bond.getDestinationAtom();
            if (destAtom.getElement() == element && bond.getType() == bondType && structure.containsKey(destAtom)) {
                atoms.add(destAtom);
            };
        };
        return atoms;
    };
}
