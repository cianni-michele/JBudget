package it.unicam.cs.mpgc.jbudget109164.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;

@ExtendWith(MockitoExtension.class)
public class MonthlyBudgetPlanTest {

    private MonthlyBudgetPlan plan;
    private YearMonth period;

    @Mock
    private Tag tag;

    @BeforeEach
    void setUp() {
        plan = new MonthlyBudgetPlan();
        period = YearMonth.of(2025, 6);
    }

    @Nested
    @DisplayName("Basic Functionality")
    class BaseFunctionality {

        @Test
        @DisplayName("Should return 0 when no expected amount set")
        void shouldReturnZeroWhenNoExpectedAmountSet() {
            assertEquals(0.0, plan.getExpectedAmount(tag, period));
        }

        @Test
        @DisplayName("Should set and get expected amount for tag and period")
        void shouldSetAndGetExpectedAmount() {
            plan.setExpectedAmount(tag, period, 100.0);
            assertEquals(100.0, plan.getExpectedAmount(tag, period));
        }

        @Test
        @DisplayName("Should return 0 for a different period without prior set")
        void shouldReturnZeroForDifferentPeriod() {
            plan.setExpectedAmount(tag, period, 200.0);
            YearMonth other = period.plusMonths(1);
            assertEquals(0.0, plan.getExpectedAmount(tag, other));
        }

        @Test
        @DisplayName("Should override previous value for same tag and period")
        void shouldOverridePreviousValue() {
            plan.setExpectedAmount(tag, period, 100.0);
            plan.setExpectedAmount(tag, period, 200.0);
            assertEquals(200.0, plan.getExpectedAmount(tag, period));
        }
    }

    @Nested
    @DisplayName("Multiple Periods Handling")
    class MultiplePeriods {
        @Test
        @DisplayName("Should retrieve correct amount for first period")
        void shouldRetrieveCorrectAmountForFirstPeriod() {
            YearMonth firstPeriod = YearMonth.of(2025, 1);
            YearMonth secondPeriod = YearMonth.of(2025, 2);
            YearMonth thirdPeriod = YearMonth.of(2025, 3);

            plan.setExpectedAmount(tag, firstPeriod, 100.0);
            plan.setExpectedAmount(tag, secondPeriod, 200.0);
            plan.setExpectedAmount(tag, thirdPeriod, 300.0);

            assertEquals(100.0, plan.getExpectedAmount(tag, firstPeriod));
        }

        @Test
        @DisplayName("Should retrieve correct amount for second period")
        void shouldRetrieveCorrectAmountForSecondPeriod() {
            YearMonth firstPeriod = YearMonth.of(2025, 1);
            YearMonth secondPeriod = YearMonth.of(2025, 2);
            YearMonth thirdPeriod = YearMonth.of(2025, 3);

            plan.setExpectedAmount(tag, firstPeriod, 100.0);
            plan.setExpectedAmount(tag, secondPeriod, 200.0);
            plan.setExpectedAmount(tag, thirdPeriod, 300.0);

            assertEquals(200.0, plan.getExpectedAmount(tag, secondPeriod));
        }

        @Test
        @DisplayName("Should retrieve correct amount for third period")
        void shouldRetrieveCorrectAmountForThirdPeriod() {
            YearMonth firstPeriod = YearMonth.of(2025, 1);
            YearMonth secondPeriod = YearMonth.of(2025, 2);
            YearMonth thirdPeriod = YearMonth.of(2025, 3);

            plan.setExpectedAmount(tag, firstPeriod, 100.0);
            plan.setExpectedAmount(tag, secondPeriod, 200.0);
            plan.setExpectedAmount(tag, thirdPeriod, 300.0);

            assertEquals(300.0, plan.getExpectedAmount(tag, thirdPeriod));
        }

        @Test
        @DisplayName("Should retrieve correct amount for first non-consecutive period")
        void shouldRetrieveCorrectAmountForFirstNonConsecutivePeriod() {
            YearMonth january2025 = YearMonth.of(2025, 1);
            YearMonth january2026 = YearMonth.of(2026, 1);

            plan.setExpectedAmount(tag, january2025, 100.0);
            plan.setExpectedAmount(tag, january2026, 200.0);

            assertEquals(100.0, plan.getExpectedAmount(tag, january2025));
        }

        @Test
        @DisplayName("Should retrieve correct amount for second non-consecutive period")
        void shouldRetrieveCorrectAmountForSecondNonConsecutivePeriod() {
            YearMonth january2025 = YearMonth.of(2025, 1);
            YearMonth january2026 = YearMonth.of(2026, 1);

            plan.setExpectedAmount(tag, january2025, 100.0);
            plan.setExpectedAmount(tag, january2026, 200.0);

            assertEquals(200.0, plan.getExpectedAmount(tag, january2026));
        }
    }

    @Nested
    @DisplayName("Multiple Tags Handling")
    class MultipleTags {
        @Test
        @DisplayName("Should retrieve correct amount for first tag")
        void shouldRetrieveCorrectAmountForFirstTag() {
            Tag anotherTag = mock(Tag.class);

            plan.setExpectedAmount(tag, period, 100.0);
            plan.setExpectedAmount(anotherTag, period, 200.0);

            assertEquals(100.0, plan.getExpectedAmount(tag, period));
        }

        @Test
        @DisplayName("Should retrieve correct amount for second tag")
        void shouldRetrieveCorrectAmountForSecondTag() {
            Tag anotherTag = mock(Tag.class);

            plan.setExpectedAmount(tag, period, 100.0);
            plan.setExpectedAmount(anotherTag, period, 200.0);

            assertEquals(200.0, plan.getExpectedAmount(anotherTag, period));
        }
    }

    @Nested
    @DisplayName("Edge and Error Cases")
    class EdgeAndErrorCases {

        @Test
        @DisplayName("Should handle negative amounts")
        void shouldHandleNegativeAmount() {
            double negativeAmount = -50.0;
            plan.setExpectedAmount(tag, period, negativeAmount);
            assertEquals(negativeAmount, plan.getExpectedAmount(tag, period));
        }

        @Test
        @DisplayName("Should throw NullPointerException when tag is null")
        void shouldThrowExceptionForNullTag() {
            assertThrows(NullPointerException.class, () ->
                    plan.setExpectedAmount(null, period, 100.0));
        }

        @Test
        @DisplayName("Should throw NullPointerException when period is null")
        void shouldThrowExceptionForNullPeriod() {
            assertThrows(NullPointerException.class, () ->
                    plan.setExpectedAmount(tag, null, 100.0));
        }
    }

    @Nested
    @DisplayName("Special Values")
    class SpecialValues {
        @Test
        @DisplayName("Should handle zero amount correctly")
        void shouldHandleZeroAmount() {
            plan.setExpectedAmount(tag, period, 0.0);
            assertEquals(0.0, plan.getExpectedAmount(tag, period));
        }

        @Test
        @DisplayName("Should handle NaN value")
        void shouldHandleNaNValue() {
            plan.setExpectedAmount(tag, period, Double.NaN);
            assertTrue(Double.isNaN(plan.getExpectedAmount(tag, period)));
        }

        @Test
        @DisplayName("Should handle positive infinity value")
        void shouldHandlePositiveInfinityValue() {
            plan.setExpectedAmount(tag, period, Double.POSITIVE_INFINITY);
            assertEquals(Double.POSITIVE_INFINITY, plan.getExpectedAmount(tag, period));
        }

        @Test
        @DisplayName("Should handle negative infinity value")
        void shouldHandleNegativeInfinityValue() {
            plan.setExpectedAmount(tag, period, Double.NEGATIVE_INFINITY);
            assertEquals(Double.NEGATIVE_INFINITY, plan.getExpectedAmount(tag, period));
        }

        @Test
        @DisplayName("Should handle max double value")
        void shouldHandleMaxDoubleValue() {
            plan.setExpectedAmount(tag, period, Double.MAX_VALUE);
            assertEquals(Double.MAX_VALUE, plan.getExpectedAmount(tag, period));
        }

        @Test
        @DisplayName("Should handle min double value")
        void shouldHandleMinDoubleValue() {
            plan.setExpectedAmount(tag, period, Double.MIN_VALUE);
            assertEquals(Double.MIN_VALUE, plan.getExpectedAmount(tag, period));
        }
    }
}