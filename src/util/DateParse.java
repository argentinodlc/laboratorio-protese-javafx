/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 *
 * @author Lucas Corrêa
 */
public class DateParse {
    
	public static Date parseDate(String date) throws ParseException {
		if (date.indexOf('-')>=0)
			date = date.replace('-', '/');
		
		String dateFormat = "dd/MM/yyyy";
		if ((date.length()>=3) && (date.charAt(2)!='/'))
			dateFormat = "yyyy/MM/dd";
		
		String formatPatern = dateFormat;
		if (date.length()==13) formatPatern = dateFormat+" HH";
		if (date.length()==16) formatPatern = dateFormat+" HH:mm";
		if (date.length()>=19) formatPatern = dateFormat+" HH:mm:ss";
		
		return DateParse.parseDate(date, formatPatern);
	}
	
	public static Date parseDate(String date, String Pattern) throws ParseException {
		SimpleDateFormat sp = new SimpleDateFormat(Pattern);
		sp.setLenient(false);
		Date data = sp.parse(date);
		return data;
	}
	
	public static Date parseDate(String date, String Pattern, Locale loc) throws ParseException {
		SimpleDateFormat sp = new SimpleDateFormat(Pattern, loc);
		sp.setLenient(false);
		return sp.parse(date);
	}

	
	/**
	 * Método que retorna uma data truncada recebendo como parametro outra data
	 * @param Date date
	 * @return Date data truncada;
	 */
	public static Date truncDate(Date date){
		if (date==null)
			return null;
		
		Calendar cIn = new GregorianCalendar();
		cIn.setTime(date);
		int ano = cIn.get(Calendar.YEAR);
		int mes = cIn.get(Calendar.MONTH);
		int dia = cIn.get(Calendar.DAY_OF_MONTH);
		
		Calendar cOut = new GregorianCalendar();
		cOut.set(ano, mes, dia, 0, 0, 0);
		cOut.set(Calendar.MILLISECOND, 0);
		
		return cOut.getTime();
	}
	
	/**
	 * Retorna o Timestamp de uma data.
	 * @param Date date
	 * @return Timestamp;
	 */
	public static Timestamp dateToTimestamp(Date date){
		if (date==null)
			return null;
		else 
			return	new Timestamp(date.getTime());
	}
	
	/**
	 * Método que retorna uma String que represente o parametro de data.
	 * O formato desta String é "dd/MM/yyyy".
	 * @param Date date
	 * @return String representando a data no formato 'dd/MM/yyyy';
	 */
	public static String dateToString(Date date){
		return DateParse.dateToString(date, "dd/MM/yyyy");
	}
	
	/**
	 * Método que retorna uma String que represente o parametro de data.
	 * O formato desta String é definido pelo parametro Pattern.
	 * @param Date date
	 * @param String Pattern : representa o formato da string de retorno podendo ser 'dd/MM/yyyy', 'dd-MM-yyyy', 'yyyy/MM/dd', etc.;
	 * @return String
	 */
	public static String dateToString(Date date, String Pattern){
		SimpleDateFormat sFormat = new SimpleDateFormat (Pattern);
		return sFormat.format(date);
	}	
	
	/**
	 * @param  String Contendo a data a ser somada 1 dia. Deve estar no formato "dd/mm/yyyy"
	 * @return String
	 * */
	public static String somaDia(String dataReferencia){
		int iDia = Integer.parseInt(dataReferencia.substring(0, 2));
		int iMes = Integer.parseInt(dataReferencia.substring(3, 5));
		int iAno = Integer.parseInt(dataReferencia.substring(6, 10));
		Calendar c = new GregorianCalendar(iAno, iMes-1, iDia);
		c.add(Calendar.DATE, 1);
		SimpleDateFormat sFormat = new SimpleDateFormat ("dd/MM/yyyy");
		dataReferencia = sFormat.format(c.getTime());
		return dataReferencia;
	}
	
	public static Date somaDias(Date date, int dias){
		return soma(date, 0, 0, dias);
	}

	public static Date somaMeses(Date date, int meses){
		return soma(date, 0, meses, 0);
	}

	public static Date somaAnos(Date date, int anos){
		return soma(date, anos, 0, 0);
	}
	
	public static Date soma(Date date, int anos, int meses, int dias){
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.add(Calendar.DATE, dias);
		c.add(Calendar.MONTH, meses);
		c.add(Calendar.YEAR, anos);

		return c.getTime();
	}
	
	public static long getMiliSecFromMinute(long minute){
		long ret = (1000*60*minute);
		return ret;
	}
	
	public static long getMiliSecFromHour(long hour){
		long ret = (1000*60*60*hour);
		return ret;
	}
	

	/**
	 * Método usado para recuperar a data inicial do ciclo atual.<br>
	 * ex: dia atual (21/10/2009 15:00:00) + dia ciclo (13) = 13/10/2009 00:00:00
	 * 
	 * @param diaCiclo dia de corte no ciclo do cliente
	 * @return Date - data inicial do ciclo
	 * */
	public static Date getDtInicioCiclo(int diaCiclo){
		GregorianCalendar calendar =  new GregorianCalendar();
		calendar.set(Calendar.DAY_OF_MONTH, diaCiclo);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		Date d = calendar.getTime();
		Date dAtual = new Date();
		
		if (dAtual.compareTo(d)<0)
			d = DateParse.somaMeses(d, -1);
		
		return d;
	}
	
	/**
	 * Metodo que recebe uma data como entrada e retorna um array de inteiros
	 * de 3 posições, que são dia, mes e ano respectivamente.
	 * @param Date d
	 * @return int[]
	 */
	public static int getDia(Date d){
		int ret = 0;
		
		Calendar c = new GregorianCalendar();
		c.setTime(d);
		
		ret = c.get(Calendar.DAY_OF_MONTH);
		
		return ret;
	}
	
	/**
	 * Metodo que recebe uma data como entrada e retorna um array de inteiros
	 * de 3 posições, que são dia, mes e ano respectivamente.
	 * @param Date d
	 * @return int[]
	 */
	public static int[] dateToDMAintArr(Date d){
		int [] retArr = new int[3];
		
		Calendar c = new GregorianCalendar();
		c.setTime(d);
		
		retArr[0] = c.get(Calendar.DAY_OF_MONTH);
		retArr[1] = c.get(Calendar.MONTH)+1;
		retArr[2] = c.get(Calendar.YEAR);
		
		return retArr;
	}
	
	/**
	 * Verificar se duas datas se encontram no mesmo dia/mes/ano
	 * não levando em conta as horas/minutos/segundos.
	 * */
	public static boolean equalsDate(Date date1, Date date2){
		boolean ret = true;
		
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		
        //Divide-se pela quantidade de milisegundos de um dia
		//e obtem-se a quantidade de dias decorridos apartir 
		//do dia 01/01/1970 00:00:00:000 até a data em questao. 
		long dia1 = time1/(1000*60*60*24);
		long dia2 = time2/(1000*60*60*24);
		
		if (dia1==dia2) ret = true;
		else ret = false;
		
		return ret;
	}
	
	public static void main(String [] args) throws ParseException{
		System.out.println("====================================");
		System.out.println("dd/MM/yyyy");
		System.out.println();
		String dateStr = "28/03/2010";
		Date date = DateParse.parseDate(dateStr, "dd/MM/yyyy");
		System.out.println(dateStr+ " = " +date);
		
		int[] dma = DateParse.dateToDMAintArr(date);
		Date ini = DateParse.somaDias(date, -(dma[0]-1) );
		ini = DateParse.somaMeses(ini, -(dma[1]-1) );
		System.out.println(ini);
		
		Date fim = DateParse.somaAnos(ini, 1);
		System.out.println(fim);
		
		
                
	}
	
	/*
	public static void main(String [] args) throws ParseException{
		System.out.println("====================================");
		System.out.println("dd/MM/yyyy");
		System.out.println();
		String date = "21/02/2010";
		System.out.println(date+ " = " +DateParse.parseDate(date));
		date = "21/02/2010 13";
		System.out.println(date+ " = " +DateParse.parseDate(date));
		date = "21/02/2010 13:45";
		System.out.println(date+ " = " +DateParse.parseDate(date));
		date = "21/02/2010 13:45:17";
		System.out.println(date+ " = " +DateParse.parseDate(date));
		date = "21/02/2010 13:45:17.2";
		System.out.println(date+ " = " +DateParse.parseDate(date));
		
		System.out.println("====================================");
		System.out.println("yyyy/MM/dd");
		System.out.println();
		date = "2010/02/21";
		System.out.println(date+ " = " +DateParse.parseDate(date));
		date = "2010/02/21 13";
		System.out.println(date+ " = " +DateParse.parseDate(date));
		date = "2010/02/21 13:45";
		System.out.println(date+ " = " +DateParse.parseDate(date));
		date = "2010/02/21 13:45:17";
		System.out.println(date+ " = " +DateParse.parseDate(date));
		date = "2010/02/21 13:45:17.2";
		System.out.println(date+ " = " +DateParse.parseDate(date));

		System.out.println("====================================");
		System.out.println("dd-MM-yyyy");
		System.out.println();
		date = "21-02-2010";
		System.out.println(date+ " = " +DateParse.parseDate(date));
		date = "21-02-2010 13";
		System.out.println(date+ " = " +DateParse.parseDate(date));
		date = "21-02-2010 13:45";
		System.out.println(date+ " = " +DateParse.parseDate(date));
		date = "21-02-2010 13:45:17";
		System.out.println(date+ " = " +DateParse.parseDate(date));
		date = "21-02-2010 13:45:17.2";
		System.out.println(date+ " = " +DateParse.parseDate(date));
		
		System.out.println("====================================");
		System.out.println("yyyy-MM-dd");
		System.out.println();
		date = "2010-02-21";
		System.out.println(date+ " = " +DateParse.parseDate(date));
		date = "2010-02-21 13";
		System.out.println(date+ " = " +DateParse.parseDate(date));
		date = "2010-02-21 13:45";
		System.out.println(date+ " = " +DateParse.parseDate(date));
		date = "2010-02-21 13:45:17";
		System.out.println(date+ " = " +DateParse.parseDate(date));
		date = "2010-02-21 13:45:17.2";
		System.out.println(date+ " = " +DateParse.parseDate(date));
		
		
		System.out.println("====================================");
		System.out.println("truncDate(date);");
		Date dateIn = new Date();
		Date dateTrunc = DateParse.truncDate(dateIn);
		System.out.println("dateIn = " +dateIn);
		System.out.println("dateTrunc = " +dateTrunc);
		System.out.println("Timestamp = " +DateParse.dateToTimestamp(dateTrunc));
		System.out.println("====================================");
		date = "2011-12-31 23:59:59";
		dateIn = DateParse.parseDate(date);
		dateTrunc = DateParse.truncDate(dateIn);
		System.out.println("dateIn = " +dateIn);
		System.out.println("dateTrunc = " +dateTrunc);
		System.out.println("Timestamp = " +DateParse.dateToTimestamp(dateTrunc));

		System.out.println("====================================");
		dateIn = null;
		dateTrunc = DateParse.truncDate(dateIn);
		System.out.println("dateIn = " +dateIn);
		System.out.println("dateTrunc = " +dateTrunc);
		System.out.println("Timestamp = " +DateParse.dateToTimestamp(dateTrunc));
		System.out.println("====================================");
	}
        */ 
}
