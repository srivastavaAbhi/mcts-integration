package org.motechproject.mcts.lookup;

public enum MCTSPregnantMotherMatchStatus {
    BLANK(0), YES(1), NO(2), UNKNOWN(3), CLOSED(4), ARCHIVE(5);

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    MCTSPregnantMotherMatchStatus(int id) {
        this.id = id;
    }
    
    public static int getID(MCTSPregnantMotherMatchStatus value) {
        
        return value.getId();
    }
}
