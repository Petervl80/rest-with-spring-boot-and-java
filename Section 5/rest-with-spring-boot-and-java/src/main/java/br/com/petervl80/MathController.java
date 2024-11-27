package br.com.petervl80;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.petervl80.converters.NumberConverter;
import br.com.petervl80.exceptions.UnssuportedMathOperationException;

@RestController
public class MathController {

	@GetMapping("/sum/{numberOne}/{numberTwo}")
	public Double sum(@PathVariable String numberOne, @PathVariable String numberTwo) throws Exception {

		if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
			throw new UnssuportedMathOperationException("Please set a numeric value");
		}
		
		return NumberConverter.convertToDouble(numberOne) + NumberConverter.convertToDouble(numberTwo);
	}
	
	@GetMapping("/subtraction/{numberOne}/{numberTwo}")
	public Double subtraction(@PathVariable String numberOne, @PathVariable String numberTwo) throws Exception {

		if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
			throw new UnssuportedMathOperationException("Please set a numeric value");
		}
		
		return NumberConverter.convertToDouble(numberOne) - NumberConverter.convertToDouble(numberTwo);
	}
	
	@GetMapping("/multiplication/{numberOne}/{numberTwo}")
	public Double multiplication(@PathVariable String numberOne, @PathVariable String numberTwo) throws Exception {

		if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
			throw new UnssuportedMathOperationException("Please set a numeric value");
		}
		
		return NumberConverter.convertToDouble(numberOne) * NumberConverter.convertToDouble(numberTwo);
	}
	
	@GetMapping("/division/{numberOne}/{numberTwo}")
	public Double division(@PathVariable String numberOne, @PathVariable String numberTwo) throws Exception {

		if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
			throw new UnssuportedMathOperationException("Please set a numeric value");
		} else if(NumberConverter.convertToDouble(numberTwo) == 0) {
			throw new UnssuportedMathOperationException("You can not divide by 0");
		}
		
		return NumberConverter.convertToDouble(numberOne) / NumberConverter.convertToDouble(numberTwo);
	}
	
	@GetMapping("/mean/{numberOne}/{numberTwo}")
	public Double mean(@PathVariable String numberOne, @PathVariable String numberTwo) throws Exception {

		if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
			throw new UnssuportedMathOperationException("Please set a numeric value");
		}
		
		return (NumberConverter.convertToDouble(numberOne) + NumberConverter.convertToDouble(numberTwo)) / 2;
	}
	
	@GetMapping("/squareRoot/{numberOne}")
	public Double squareRoot(@PathVariable String number) throws Exception {
		
		if(!NumberConverter.isNumeric(number)) {
			throw new UnssuportedMathOperationException("Please set a numeric value");
		}
		
		return Math.sqrt(NumberConverter.convertToDouble(number));
	}

}
