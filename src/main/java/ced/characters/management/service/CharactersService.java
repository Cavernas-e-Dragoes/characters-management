package ced.characters.management.service;

import ced.characters.management.helper.JwtHelper;
import ced.characters.management.models.CharacterSheet;
import ced.characters.management.repository.CharClassRepository;
import ced.characters.management.repository.CharactersRepository;
import ced.characters.management.repository.RaceRepository;
import ced.characters.management.vo.CharactersSheetDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CharactersService {

    private final CharactersRepository charactersRepository;

    private final RaceRepository raceInterface;

    private final CharClassRepository charClassRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public CharactersService(CharactersRepository charactersRepository, RaceRepository raceInterface, CharClassRepository charClassRepository) {
        this.charactersRepository = charactersRepository;
        this.raceInterface = raceInterface;
        this.charClassRepository = charClassRepository;
    }

    public List<CharactersSheetDTO> findAll(String authorization) {

        List<CharacterSheet> characterSheetList = charactersRepository.findAllByLogin(JwtHelper.findUser(authorization));
        List<CharactersSheetDTO> charactersSheetDTOS = new ArrayList<>();

        for (CharacterSheet characterSheet : characterSheetList) {

            CharactersSheetDTO charactersSheetDTO = convertToDto(characterSheet);
            charactersSheetDTO.setRaceName(raceInterface.findById(characterSheet.getRace()).get().getName());
            charactersSheetDTO.setClassName(charClassRepository.findById(characterSheet.getCharClass()).get().getName());

            charactersSheetDTOS.add(charactersSheetDTO);
        }

        return charactersSheetDTOS;
    }


    private CharactersSheetDTO convertToDto(CharacterSheet characterSheet) {
        return modelMapper.map(characterSheet, CharactersSheetDTO.class);
    }

}