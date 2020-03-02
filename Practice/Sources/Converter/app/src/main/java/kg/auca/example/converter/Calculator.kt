package kg.auca.example.converter

import java.math.BigDecimal
import java.util.*

internal class Calculator {
    enum class Operation {
        NONE, ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO
    }

    private lateinit var operands: Stack<BigDecimal>
    private var operation: Operation? = null
    private var topOperandHasDecimalPoint = false
    val currentValue: BigDecimal
        get() = operands.peek()

    init {
        reset()
    }

    fun reset() {
        operands = Stack()
        operands.push(BigDecimal.ZERO)
        operation = Operation.NONE
        topOperandHasDecimalPoint = false
    }

    fun negate() {
        operands.push(operands.pop().negate())
    }

    fun addDecimalPoint() {
        topOperandHasDecimalPoint = true
    }

    fun addDigit(digit: Int) {
        if (operation != Operation.NONE && operands.size == 1) {
            operands.push(BigDecimal.ZERO)
            topOperandHasDecimalPoint = false
        }
        var operand = operands.pop()
        if (operand.scale() == 0 && !topOperandHasDecimalPoint) {
            operand = operand.multiply(BigDecimal.TEN)
            operand = if (operand < BigDecimal.ZERO) operand.subtract(BigDecimal(digit)) else operand.add(BigDecimal(digit))
        } else {
            val scale = operand.scale() + 1
            val fraction = BigDecimal(digit).movePointLeft(scale)
            operand = operand.setScale(scale, BigDecimal.ROUND_UNNECESSARY)
            operand = if (operand < BigDecimal.ZERO) operand.subtract(fraction) else operand.add(fraction)
        }
        operands.push(operand)
    }

    @Throws(ArithmeticException::class)
    fun performBinaryOperation(binaryOperation: Operation?) {
        if (operands.size > 1) {
            val secondOperand = operands.pop()
            val firstOperand = operands.pop()
            when (binaryOperation) {
                Operation.ADD -> operands.push(firstOperand.add(secondOperand))
                Operation.SUBTRACT -> operands.push(firstOperand.subtract(secondOperand))
                Operation.MULTIPLY -> operands.push(firstOperand.multiply(secondOperand))
                Operation.DIVIDE, Operation.MODULO -> if (secondOperand == BigDecimal.ZERO) {
                    operands.push(BigDecimal.ZERO)
                    throw ArithmeticException()
                } else {
                    val roundingPrecision = 20
                    val result = if (binaryOperation == Operation.DIVIDE) firstOperand.divide(
                            secondOperand,
                            roundingPrecision,
                            BigDecimal.ROUND_HALF_EVEN
                    ) else firstOperand.divideAndRemainder(
                            secondOperand
                    )[1]
                    operands.push(result)
                }
                else -> {}
            }
            var operand = operands.pop()
            operand = operand.stripTrailingZeros()
            operands.push(operand)
            topOperandHasDecimalPoint = operands.peek().scale() > 0
        }
        operation = binaryOperation
    }

    @Throws(ArithmeticException::class)
    fun calculate() {
        if (operation != Operation.NONE) {
            performBinaryOperation(operation)
        }
    }
}