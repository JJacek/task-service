package com.company.taskservice.domain.task.processor.hammingdistance;

public class SimpleHammingDistance implements HammingDistance {

    private final org.apache.commons.text.similarity.HammingDistance hammingDistance;

    public SimpleHammingDistance() {
        this.hammingDistance = new org.apache.commons.text.similarity.HammingDistance();
    }

    @Override
    public int countDistance(CharSequence left, CharSequence right) {
        return hammingDistance.apply(left, right);
    }

}
