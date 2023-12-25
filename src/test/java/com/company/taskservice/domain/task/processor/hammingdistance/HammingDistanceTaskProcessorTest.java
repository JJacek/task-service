package com.company.taskservice.domain.task.processor.hammingdistance;

import com.company.taskservice.domain.task.model.Task;
import com.company.taskservice.domain.task.processor.TaskProcessorDelayer;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.AdditionalMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class HammingDistanceTaskProcessorTest {
    private static final float FLOAT_DELTA = 0.000001f;
    private HammingDistance hammingDistance;
    private TaskProcessorDelayer taskProcessorDelayer;
    private Consumer<Task> statusChangedConsumer;
    private HammingDistanceTaskProcessor hammingDistanceTaskProcessor;

    @BeforeEach
    public void setup() {
        hammingDistance = Mockito.mock(HammingDistance.class);
        taskProcessorDelayer = Mockito.mock(TaskProcessorDelayer.class);
        statusChangedConsumer = Mockito.mock(Consumer.class);
        hammingDistanceTaskProcessor = new HammingDistanceTaskProcessor(hammingDistance, taskProcessorDelayer, 0);
    }

    @ParameterizedTest
    @MethodSource("provideDataForProcessTask")
    void processTask(Task spiedTask,
                     int expectedPosition,
                     int expectedTypos,
                     List<Pair<String, Integer>> extractedPatternFromInputAndDistanceForMock,
                     int expectedStatusChangedConsumerInvocations,
                     List<Float> expectedStatusesChangedInOrder) {
        Mockito.doNothing().when(taskProcessorDelayer).delayIteration(anyLong());
        extractedPatternFromInputAndDistanceForMock.forEach(pair ->
            Mockito.when(hammingDistance.countDistance(pair.getLeft(), spiedTask.getPattern())).thenReturn(pair.getRight())
        );

        hammingDistanceTaskProcessor.processTask(spiedTask, statusChangedConsumer);

        assertThat(spiedTask.getTaskResult().getPosition()).isEqualTo(expectedPosition);
        assertThat(spiedTask.getTaskResult().getTypos()).isEqualTo(expectedTypos);
        assertThat(spiedTask.getStatus()).isEqualTo(Task.STATUS_COMPLETED);
        Mockito.verify(statusChangedConsumer, times(expectedStatusChangedConsumerInvocations)).accept(spiedTask);
        InOrder inOrderSpiedTask = Mockito.inOrder(spiedTask);
        expectedStatusesChangedInOrder.forEach(expectedStatus ->
                inOrderSpiedTask.verify(spiedTask).setStatus(AdditionalMatchers.eq(expectedStatus, FLOAT_DELTA)));
    }

    static Stream<Arguments> provideDataForProcessTask() {
        return Stream.of(
                Arguments.of(Mockito.spy(Task.builder().input("ABCD").pattern("BCD").build()), 1, 0,
                        List.of(
                            Pair.of("ABC", 3),
                            Pair.of("BCD", 0)),
                        2,
                        List.of(0.5f, Task.STATUS_COMPLETED)),
                Arguments.of(Mockito.spy(Task.builder().input("ABCD").pattern("BWD").build()), 1, 1,
                        List.of(
                            Pair.of("ABC", 3),
                            Pair.of("BCD", 1)),
                        2,
                        List.of(0.5f, Task.STATUS_COMPLETED)),
                Arguments.of(Mockito.spy(Task.builder().input("ABCDEFG").pattern("CFG").build()), 4, 1,
                        List.of(
                            Pair.of("ABC", 3),
                            Pair.of("BCD", 3),
                            Pair.of("CDE", 2),
                            Pair.of("DEF", 3),
                            Pair.of("EFG", 1)),
                        5,
                        List.of(0.2f, 0.4f, 0.6f, 0.8f, Task.STATUS_COMPLETED)),
                Arguments.of(Mockito.spy(Task.builder().input("ABCABC").pattern("ABC").build()), 0, 0,
                        List.of(
                            Pair.of("ABC", 0)),
                        1,
                        List.of(Task.STATUS_COMPLETED)),
                Arguments.of(Mockito.spy(Task.builder().input("ABCDEFG").pattern("TDD").build()), 1, 2,
                        List.of(
                            Pair.of("ABC", 3),
                            Pair.of("BCD", 2),
                            Pair.of("CDE", 2),
                            Pair.of("DEF", 3),
                            Pair.of("EFG", 3)),
                        5,
                        List.of(0.2f, 0.4f, 0.6f, 0.8f, Task.STATUS_COMPLETED)),
                Arguments.of(Mockito.spy(Task.builder().input("ABCDE").pattern("XYZ").build()), 0, 3,
                        List.of(
                            Pair.of("ABC", 3),
                            Pair.of("BCD", 3),
                            Pair.of("CDE", 3)),
                        3,
                        List.of(0.333333f, 0.666666f, Task.STATUS_COMPLETED))
        );
    }

    @Test
    void processTaskShouldReturnIllegalArgumentException() {
        Task task = Task.builder().input("ABCD").pattern("BCD").status(0.5f).build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            hammingDistanceTaskProcessor.processTask(task, statusChangedConsumer);
        });
    }

}